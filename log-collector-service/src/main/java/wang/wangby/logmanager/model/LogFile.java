
package wang.wangby.logmanager.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.annotation.persistence.Length;
import wang.wangby.annotation.persistence.Table;
import wang.wangby.model.dao.BaseModel;

@Data
@Table("t_logFile")
@Remark("日志文件")
public class LogFile extends BaseModel {
    @Id
    @Remark("主键")
    private Long logFileId;

    @Remark("所在服务器")
    private Long serverInfoId;

    @Length(255)
    @Remark("所在目录")
    private String path;

    @Length(100)
    @Remark("匹配规则")
    private String fileNameRegx;

    @Length(32)
    @Remark("用户名")
    private String username;

    @Length(32)
    @Remark("密码")
    private String passwd;

    @Length(10)
    @Remark("文件编码")
    private String charset;

    @Remark("开始")
    private String beginPattern;

    @Remark("日期格式")
    private String dateFormat;

   transient private ServerInfo serverInfo;
}
