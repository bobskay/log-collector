package wang.wangby.utils.shell;

import com.jcraft.jsch.JSchException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class ShellTest {

    @Test
    public void exec() throws JSchException, IOException {
        @Cleanup
        Shell shell = new Shell("myos", "root", "root");
        String s = shell.exec("ls /media/sf_linux/ ");
        log.debug("shell执行结果\n" + s);

    }

    @Test
    public void run() throws JSchException, IOException, InterruptedException {
        Shell shell = new Shell("myos", "root", "root");
        String command="tail -f /opt/logtask/logs/1.txt";
        shell.run(command,str->{
            log.debug(str);
        });
    }

    @Test
    public void getPid() throws JSchException, IOException {
        Shell shell = new Shell("myos", "root", "root");
        log.debug("pid="+shell.getPid(53));
    }

}