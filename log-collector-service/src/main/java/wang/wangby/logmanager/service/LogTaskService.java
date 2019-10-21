package wang.wangby.logmanager.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.wangby.dao.BaseDao;
import wang.wangby.exception.Message;
import wang.wangby.logmanager.dao.LogTaskDao;
import wang.wangby.logmanager.dao.LogTaskDetailDao;
import wang.wangby.logmanager.model.LogTask;
import wang.wangby.logmanager.model.LogTaskDetail;
import wang.wangby.logmanager.model.vo.FileReadInfo;
import wang.wangby.logmanager.model.vo.TaskRunningInfo;
import wang.wangby.service.BaseService;
import wang.wangby.utils.IdWorker;
import wang.wangby.utils.shell.Shell;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.utils.shell.file.FileInfo;
import wang.wangby.utils.shell.file.RemoteFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class LogTaskService extends BaseService<LogTask> {

    @Autowired
    LogTaskDao logTaskDao;
    @Autowired
    LogTaskDetailDao logTaskDetailDao;
    @Autowired
    ShellClient shellClient;
    @Autowired
    LogFileService logFileService;
    @Autowired
    LogTaskService logTaskService;

    public BaseDao defaultDao() {
        return logTaskDao;
    }

    public LogTask newModel() {
        return new LogTask();
    }

    public List<LogTaskDetail> getDetail(Long logTaskId) {
        LogTaskDetail query=new LogTaskDetail();
        query.setLogTaskId(logTaskId);
        return logTaskDetailDao.select(query);
    }

    @Transactional
    public void updateTask(LogTask task, LogTaskDetail logTaskDetail) {
        logTaskDetailDao.updateById(logTaskDetail);
        logTaskDao.updateById(task);
    }

    //获得任务的执行清情况
    public TaskRunningInfo runningInfo(LogTask logTask){
        TaskRunningInfo info = new TaskRunningInfo();
        String command = "lsof -i:" + logTask.getRunningPort();
        try {
            String result = shellClient.exec(command, logTask.getRunningServer(), logTask.getUsername(), logTask.getPasswd());
            info.setPortInfo(result.trim());
            List<FileReadInfo> fileReadInfos = logFileService.fileReadInfo(logTask.getLogFileId());
            info.setFileReadInfos(fileReadInfos);
        } catch (Exception ex) {
            info.setLoginInfo("验证出错:" + ex.getMessage());
            log.error(ex.getMessage(),ex);
        }
        return info;
    }


    public void insert(LogTask logTask) {
        logTask.setCreateTime(new Date());
        super.insert(logTask);
    }

    /**
     * jarFile 允许程序所在路径
     * */
    public LogTask run(Long logTaskId,String jarFile) throws JSchException, SftpException, IOException {
        LogTask logTask=this.get(logTaskId);
        Shell shell=shellClient.getShell(logTask.getRunningServer(),logTask.getUsername(),logTask.getPasswd());
        String name="logTask"+IdWorker.nextString()+".jar";
        String target=logTask.getWorkdir()+"/"+name;
        logTask.setRunningApp(name);
        shell.upload(jarFile,target);

        RemoteFile remoteFile=new RemoteFile(shell);
        FileInfo file=remoteFile.getFileInfo(target);
        if(file==null){
            throw new Message("上传程序文件出错");
        }
        shell.exec("java -jar "+target+" "+crateArg(logTask));
        this.updateField("appName",name,logTask.getRunningApp(),logTaskId);
        return logTask;
    }

    private String crateArg(LogTask logTask){
        StringBuilder sb=new StringBuilder();
        sb.append(" --my.task.taskId="+logTask.getLogTaskId());
        sb.append(" --my.task.serverName="+logTask.getRunningServer());
        sb.append(" --server.port="+logTask.getRunningPort());
        return sb.toString();
    }
}
