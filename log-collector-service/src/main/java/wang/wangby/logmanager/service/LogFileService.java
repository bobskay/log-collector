package wang.wangby.logmanager.service;


import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.wangby.dao.BaseDao;
import wang.wangby.logmanager.dao.LogFileDao;
import wang.wangby.logmanager.model.LogFile;
import wang.wangby.logmanager.model.LogTaskDetail;
import wang.wangby.logmanager.model.ServerInfo;
import wang.wangby.logmanager.model.vo.FileReadInfo;
import wang.wangby.service.BaseService;
import wang.wangby.utils.CollectionUtil;
import wang.wangby.utils.shell.Shell;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.utils.shell.file.FileInfo;
import wang.wangby.utils.shell.file.RemoteFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogFileService extends BaseService<LogFile> {

    @Autowired
    ShellClient shellClient;

    @Autowired
    LogFileDao logFileDao;
    @Autowired
    ServerInfoService serverInfoService;
    @Autowired
    LogTaskDetailService logTaskDetailService;


    @Override
    public BaseDao defaultDao() {
        return logFileDao;
    }

    @Override
    public LogFile newModel() {
        return new LogFile();
    }

    public List<FileReadInfo> fileReadInfo(Long logFileId) throws JSchException, IOException {
        LogFile logFile=this.get(logFileId);
        ServerInfo server=serverInfoService.get(logFile.getServerInfoId());
        Shell shell=shellClient.getShell(server.getHostName(),logFile.getUsername(),logFile.getPasswd());
        RemoteFile remoteFile=new RemoteFile(shell);
        List<FileInfo> list= remoteFile.getFileInfo(logFile.getPath(), name->name.matches(logFile.getFileNameRegx()));

        List<String> files= CollectionUtil.pickUp(list,FileInfo::getFileName);
        List<LogTaskDetail> details=$(logTaskDetailService).attr("fileIdentify").in(files).list();

        List<FileReadInfo> read=new ArrayList<>();
        for(FileInfo file:list){
            FileReadInfo info=new FileReadInfo();
            info.setFileInfo(file);
            for(LogTaskDetail detail:details){
                if(file.getFileName().equalsIgnoreCase(detail.getFileIdentify())){
                    info.setLogTaskDetail(detail);
                    break;
                }
            }
            read.add(info);
        }
        return read;
    }
}
