package wang.wangby.logmanager.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.annotation.persistence.Length;
import wang.wangby.annotation.persistence.Table;
import wang.wangby.model.dao.BaseModel;

import java.util.Date;
@Data
@Table("t_logTask")
@Remark("")
public class LogTask extends BaseModel{
    public  static enum  State{
        running,stop
    }

    @Id
    @Remark("主键")
    private Long logTaskId;

    @Remark("文件id")
    private Long logFileId;

    @Remark("创建时间")
    private Date createTime;

    @Remark("最后更新时间")
    private Date updateTime;

    @Length(20)
    @Remark("日志类型")
    private String logType;

    @Length(10)
    @Remark("任务状态")
    private String taskState;

    @Length(100)
    @Remark("执行任务的服务器")
    private String runningServer;

    @Remark("执行任务所用端口")
    private Integer runningPort;

    @Length(255)
    @Remark("执行任务的程序")
    private String runningApp;

    @Length(32)
    @Remark("执行任务的用户名")
    private String username;

    @Length(32)
    @Remark("执行任务的密码")
    private String passwd;

    @Length(255)
    @Remark("执行任务的目录")
    private String workdir;

    @Remark("每次读取间隔")
    private Integer intervalSecond;

    @Remark("第一次运行延时")
    private Integer delaySecond;

    @Remark("单次读取字节数")
    private Integer batchSzie;


}
