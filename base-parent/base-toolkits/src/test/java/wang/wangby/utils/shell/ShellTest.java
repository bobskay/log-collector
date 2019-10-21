package wang.wangby.utils.shell;

import com.jcraft.jsch.JSchException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ShellTest {

    @Test
    public void exec() throws JSchException, IOException {
        @Cleanup
        Shell shell = new Shell("192.168.1.114", "root", "root");
        String s = shell.exec("ls /media/sf_linux/ ");
        log.debug("shell执行结果\n" + s);

    }

    @Test
    public void run() throws JSchException, IOException {
        Shell shell = new Shell("192.168.1.114", "root", "root");
        String command="tail -f /opt/logtask/logs/1.txt";

        shell.runInChannel(command,ch->{
            new Thread(()->{
              try{

                  Thread.sleep(2000);
                    ch.sendSignal("nal");
                    ch.disconnect();

              }catch (Exception ex){
                  ex.printStackTrace();
              }
            }).start();

           try{
               InputStream in=ch.getInputStream();
               BufferedReader reader=new BufferedReader(new InputStreamReader(in));
              while(true){
                  String i=reader.readLine();
                  if(i==null){
                      break;
                  }
                  System.out.print(i);
              }
           }catch (Exception ex){

           }
        });
    }

    @Test
    public void execBatch() throws Exception {
        long begin = System.currentTimeMillis();
        @Cleanup
        Shell shell = new Shell("192.168.1.114", "root", "root");
        log.debug("连接用时:" + (System.currentTimeMillis() - begin));
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                    long bb = System.currentTimeMillis();
                    String result = shell.exec("ls /home");
                    log.debug((System.currentTimeMillis() - bb) + "");
                } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                }
            }).start();
        }
        latch.countDown();
        Thread.sleep(1000);

    }
}