package wang.wangby.logtask.service;

import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wang.wangby.logmanager.model.LogFile;
import wang.wangby.logmanager.model.LogTask;
import wang.wangby.logmanager.model.LogTaskDetail;
import wang.wangby.logmanager.model.ServerInfo;
import wang.wangby.logmanager.service.LogFileService;
import wang.wangby.logmanager.service.LogTaskDetailService;
import wang.wangby.logmanager.service.LogTaskService;
import wang.wangby.logmanager.service.ServerInfoService;
import wang.wangby.logtask.TaskConfig;
import wang.wangby.logtask.model.LogParseResult;
import wang.wangby.logtask.parser.LogParser;
import wang.wangby.logtask.parser.LogbackParser;
import wang.wangby.logtask.processor.LogProcessor;
import wang.wangby.utils.shell.file.FileInfo;
import wang.wangby.utils.shell.file.RemoteFile;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LogReaderService implements InitializingBean {

    @Autowired
    private TaskConfig taskConfig;
    private RemoteFile remoteFile;
    @Value("${server.port}")
    private Integer port;
    LogFile logFile;
    LogTask logTask;
    JobInfo jobInfo;
    Charset charset;
    LogParser logParser;

    @Autowired
    LogTaskService logTaskService;
    @Autowired
    LogFileService logFileService;
    @Autowired
    ServerInfoService serverInfoService;
    @Autowired
    LogTaskDetailService logTaskDetailService;
    @Autowired
    LogProcessor logProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            LogTask task = check();
            logFile = logFileService.get(task.getLogFileId());
            ServerInfo serverInfo = serverInfoService.get(logFile.getServerInfoId());
            remoteFile = new RemoteFile(serverInfo.getIpAddr(), logFile.getUsername(), logFile.getPasswd());
            logParser=new LogbackParser(logFile.getBeginPattern(),logFile.getDateFormat(),logFile.getCharset());

            jobInfo = ScheduledFactory.newSchedule(this::run, "LogTaskController", task.getDelaySecond(),
                    task.getIntervalSecond(), TimeUnit.SECONDS);
        }catch (Exception ex){
            log.error("任务启动失败,3秒后退出",ex);
            Thread.sleep(3000);
            System.exit(1);
        }

    }

    public void run() {
        logTask = check();
        List<LogTaskDetail> list = logTaskService.getDetail(logTask.getLogTaskId());
        try {
            charset = Charset.forName(logFile.getCharset());
            LogTaskDetail detailInfo = getEarlyFile(list);
            if (detailInfo == null) {
                return;
            }
            doRead(detailInfo,logTask);
        } catch (Exception ex) {
            log.error("执行文件读取任务出错:" + ex.getMessage(), ex);
        }
    }

    //开始读取文件
    private void doRead(LogTaskDetail logTaskDetail,LogTask task) throws Exception {
        String logText = remoteFile.read(logTaskDetail.getFileIdentify(), logTaskDetail.getReadLength() + 1, task.getBatchSzie());
        int readSize=logText.getBytes(charset).length;
        log.info("本次读到数据:"+readSize);
        boolean isEnd = false;
        //读到的数据小于期望的说明到文件末尾了
        if ( readSize< task.getBatchSzie()) {
            isEnd = true;
        }
        LogParseResult result = logParser.parse(logTaskDetail.getReadLength() + 1, logText, isEnd);
        //检查在读取文件期间任务是否被外部修改
         task = check();
        if (logProcessor.doProcess(result)) {
            task.setUpdateTime(new Date());
            logTaskDetail.setUpdateTime(new Date());
            logTaskDetail.setReadLength(result.getEnd());
            logTaskService.updateTask(task, logTaskDetail);
        }
    }

    //获取最早要读取的文件
    private LogTaskDetail getEarlyFile(List<LogTaskDetail> details) throws IOException, JSchException {
        String[] files = remoteFile.ls(logFile.getPath());
        Set<String> matchFiles = new HashSet<>();
        for (String name : files) {
            if (!name.matches(logFile.getFileNameRegx())) {
                continue;
            }
            matchFiles.add(logFile.getPath() + "/" + name);
        }
        List<FileInfo> fileList = remoteFile.getFileInfo(matchFiles);
        FileInfo earlyFile = null;
        LogTaskDetail earlyDetail = null;
        for (FileInfo f : fileList) {
            LogTaskDetail detail = getDetailInfo(details, f);
            // 跳过已经读过的
            if (detail != null && detail.getReadLength() >= f.getSize()) {
                continue;
            }
            if (earlyFile == null || earlyFile.getModify().getTime() > f.getModify().getTime()) {
                earlyFile = f;
                earlyDetail = detail;
            }
        }
        if (earlyFile == null) {
            return null;
        }
        //读到新文件
        if (earlyDetail == null) {
            LogTaskDetail detail = new LogTaskDetail();
            detail.setLogTaskId(logTask.getLogTaskId());
            detail.setCrateTime(new Date());
            detail.setFileIdentify(earlyFile.getFileName());
            detail.setFileType(LogTaskDetail.FileType.simple + "");
            detail.setReadLength(0L);
            detail.setUpdateTime(new Date());
            logTaskDetailService.insert(detail);
            return detail;
        }
        return earlyDetail;
    }

    //获取已读文件信息
    private LogTaskDetail getDetailInfo(List<LogTaskDetail> details, FileInfo fileInfo) {
        if (details == null || details.size() == 0) {
            return null;
        }

        for (LogTaskDetail detail : details) {
            if (detail.getFileIdentify().equalsIgnoreCase(fileInfo.getFileName())) {
                return detail;
            }
        }
        return null;
    }


    //检查当前执行任务的程序和系统配置的是否相同,如果发现不同就退出
    private LogTask check() {
        LogTask task = logTaskService.get(taskConfig.getTaskId());
        if(!LogTask.State.running.toString().equals(task.getTaskState())){
            log.error("任务状态不是运行中:" + task.getTaskState());
            System.exit(1);
        }
        if (task == null) {
            log.error("通过任务id无法找到任务信息:" + taskConfig.getTaskId());
            System.exit(1);
        }
        if (!task.getRunningServer().equalsIgnoreCase(taskConfig.getServerName())) {
            log.error("服务器不匹配:{}!={}", task.getRunningServer(), taskConfig.getServerName());
            System.exit(1);
        }
        if (task.getRunningPort() != port.intValue()) {
            log.error("端口不匹配:{}!={}", task.getRunningPort(), port);
            System.exit(1);
        }
        return task;
    }


}
