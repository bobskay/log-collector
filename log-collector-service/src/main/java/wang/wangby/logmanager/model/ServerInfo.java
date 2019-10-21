package wang.wangby.logmanager.model;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.annotation.persistence.Length;
import wang.wangby.annotation.persistence.Table;
import wang.wangby.model.dao.BaseModel;

@Data
@Table("t_serverInfo")
@Remark("服务器信息")
public class ServerInfo extends BaseModel {
    @Id
    @Remark("主键")
    private Long serverInfoId;

    @Length(255)
    @Remark("名称")
    private String serverName;

    @Length(255)
    @Remark("主机名")
    private String hostName;

    @Length(15)
    @Remark("ip")
    private String ipAddr;

}
