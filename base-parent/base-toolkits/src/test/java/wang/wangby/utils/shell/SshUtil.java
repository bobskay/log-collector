package wang.wangby.utils.shell;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SshUtil {

    public static ThreadPool pool = ThreadPoolFactory.newPool(SshUtil.class, 10);

    public static void exec(ShellEvent event) {
        pool.execute(event);
    }

    public static void main(String[] args) throws JSchException, IOException {

        JSch jsch = new JSch();
        final Session session = jsch.getSession("root", "192.168.1.110", 22);
        session.setPassword("root");
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        final ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand("docker logs zkmanager");
        channel.connect();
        InputStream in = channel.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String buf = null;

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                channel.disconnect();
                session.disconnect();
            }

        }.start();


        while ((buf = reader.readLine()) != null) {
            System.out.println(buf);
        }
        System.out.println("end");
    }
}
