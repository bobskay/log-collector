package wang.wangby.utils.shell.file;

import com.jcraft.jsch.JSchException;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.utils.DateTime;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoteFileTest extends TestBase {

    @Test
    public void getFileInfo() throws JSchException, IOException {
        RemoteFile remoteFile=new RemoteFile("myos","root","root");
        String[] file=remoteFile.ls("/home/logs");
        Set set=new HashSet();
        for(String s:file){
            set.add("/home/logs/"+s);
        }
        List<FileInfo> list=remoteFile.getFileInfo(set);
        for(FileInfo f:list){
            log.debug(f.getFileName()+" "+f.getSize()+" "+new DateTime(f.getModify()));
        }
    }
}