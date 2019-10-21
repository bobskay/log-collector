package wang.wangby.logmanager.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.annotation.persistence.Length;
import wang.wangby.annotation.persistence.Table;
import wang.wangby.model.dao.BaseModel;

import java.util.Date;
@Data
@Table("t_logTaskDetail")
@Remark("")
public class LogTaskDetail extends BaseModel{
    public static enum FileType{
        simple
    }

    @Id
    @Remark("主键")
    private Long logTaskDetailId;

    @Remark("任务主键")
    private Long logTaskId;

    @Remark("创建时间")
    private Date crateTime;

    @Remark("最后修改时间")
    private Date updateTime;

    @Length(255)
    @Remark("文件唯一标识")
    private String fileIdentify;

    @Remark("已读大小")
    private Long readLength;

    @Length(10)
    @Remark("文件类型")
    private String fileType;

}
