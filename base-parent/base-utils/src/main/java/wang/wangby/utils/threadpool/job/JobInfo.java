package wang.wangby.utils.threadpool.job;

import lombok.Data;
import wang.wangby.annotation.Remark;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

@Data
@Remark("计划任务信息")
public class JobInfo {

    private ScheduledExecutorService scheduledExecutorService;
    private long initialDelayMs;
    private long periodMs;
    private String name;
    private boolean running;
    private Date lastBegin;
    private Date lastEnd;
    private Integer count=0;//已经执行次数
    private String createInfo;

    public void begin() {
        count++;
        lastBegin = new Date();
    }

    public void end() {
        lastEnd = new Date();
    }
}
