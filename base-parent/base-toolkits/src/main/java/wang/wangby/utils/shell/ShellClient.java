package wang.wangby.utils.shell;

import com.jcraft.jsch.JSchException;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ShellClient {
    private static ShellClient INSTANCE = new ShellClient(60*10,"default");

    private Map<String, Shell> map = new ConcurrentHashMap<>();
    private JobInfo jobInfo;
    private long maxIdleSecond;

    public ShellClient(long maxIdleSecond,String name) {
        this.maxIdleSecond=maxIdleSecond;
        jobInfo = ScheduledFactory.newSchedule(this::check, name+"-ShellClient", 5,maxIdleSecond/10, TimeUnit.SECONDS);
    }

    public static ShellClient getDefault(){
        return INSTANCE;
    }

    public void check() {
        map.forEach((key, shell) -> {
            if (System.currentTimeMillis() - shell.getLastTime() > maxIdleSecond) {
                shell.close();
                map.remove(key);
            }
        });
    }

    public  String exec(String command, String host, String username, String password) throws JSchException, IOException {
        Shell shell=getShell(host,username,password);
        String result= shell.exec(command);
        if(StringUtil.isEmpty(result)){
            return "";
        }
        return result;
    }


    public Shell getShell(String host,String username,String password) throws JSchException {
        String key = host + "@" + username + password;
        Shell shell = map.get(key);
        if(shell!=null){
            return shell;
        }
        synchronized (map){
            if (shell == null) {
                shell = new Shell(host, username, password);
                INSTANCE.map.put(key, shell);
            }
            return shell;
        }
    }
}
