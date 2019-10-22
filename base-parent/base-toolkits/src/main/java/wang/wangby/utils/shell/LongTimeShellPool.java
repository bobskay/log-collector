package wang.wangby.utils.shell;

import com.jcraft.jsch.JSchException;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.annotation.Remark;
import wang.wangby.utils.threadpool.Event;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolFactory;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Remark("需要长时间运行的shell")
@Slf4j
public class LongTimeShellPool {
    private ThreadPool threadPool;
    private ShellClient shellClient;
    private Map<String, ShellExecInfo> running;
    private int maxIdleSecond;
    private JobInfo removeJob;

    public LongTimeShellPool(ShellClient shellClient, int size, int maxIdleSecond) {
        this.shellClient = shellClient;
        threadPool = ThreadPoolFactory.newPool("longTimeShell", size);
        running = new ConcurrentHashMap<>();
        this.maxIdleSecond = maxIdleSecond;
        removeJob = ScheduledFactory.newSchedule(this::removeIdel, "LongTimeShelRemove", 10, 30, TimeUnit.SECONDS);
    }

    public void removeIdel() {
        running.forEach((key, info) -> {
            if (System.currentTimeMillis() - info.getLastRead() > maxIdleSecond * 1000) {
                if (info.isDaemon()) {
                    return;
                }
                if (this.kill(key)) {
                    log.info("shell长时间未处理,停止任务:" + info.getCommand());
                }
            }
        });

    }

    public ShellExecInfo addShell(String command, String host, String username, String password) throws Exception {
        return addShell(command, host, username, password, false, true);
    }

    /**
     * @param daemon 是否需要手动关闭
     * @param output 是否读取输出流
     */
    public ShellExecInfo addShell(String command, String host, String username, String password, boolean daemon, boolean output) throws Exception {
        ArrayBlockingQueue queue = new ArrayBlockingQueue(1024);
        Shell shell = shellClient.getShell(host, username, password, false);

        ShellExecInfo info = new ShellExecInfo();
        info.setDaemon(daemon);
        info.setCommand(command.trim());
        info.setDatas(queue);
        info.setShell(shell);

        CountDownLatch latch = new CountDownLatch(1);
        threadPool.execute(new Event() {
            @Override
            public void execute() throws Exception {
                try {
                    if (!output) {
                        shell.run(command.trim(), null);
                        latch.countDown();
                        return;
                    }
                    shell.run(command.trim(), str -> {
                        latch.countDown();
                        queue.offer(str);
                    });
                } catch (Exception ex) {
                    log.error("执行shell出错:", ex);
                } finally {
                    latch.countDown();
                }

            }
        });
        latch.await();
        List<String> pids = new ArrayList<>();
        try {
            pids = shell.getPid(command);
        } catch (JSchException ex) {
            if ("channel is not opened.".equals(ex.getMessage())) {
                log.warn("链接关闭,重试1次");
                Shell newShell = shellClient.getShell(host, username, password, true);
                pids = newShell.getPid(command);
            } else {
                throw ex;
            }
        }

        for (String pid : pids) {
            String key = host + pid;
            if (running.get(key) == null) {
                info.setPid(pid);
                info.setKey(key);
                running.put(key, info);
                return info;
            }
        }
        return info;
    }

    public ShellExecInfo get(String id) {
        return running.get(id);
    }

    //获取shell的返回内容,如果返回null说明shell已经被关闭了
    public List<String> poll(String key) {
        ShellExecInfo info = this.get(key);
        if (info == null) {
            return null;
        }
        return info.poll();
    }

    public boolean kill(String id) {
        ShellExecInfo info = this.get(id);
        if (info == null) {
            log.info("任务不存在:" + id);
            return true;
        }
        try {
            running.remove(id);
            info.getShell().kill(info.getPid());
            return true;
        } catch (Exception e) {
            log.error("执行kill命令出错", e);
            return false;
        }
    }
}
