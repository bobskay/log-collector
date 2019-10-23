package wang.wangby.utils.dao.model;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
@Remark("数据库信息")
public class DatabaseInfo {

	@Remark("用户名")
	private String username;
	@Remark("密码")
	private String password;
	@Remark("服务器IP/host")
	private String host;
	@Remark("数据库类型")
	private String type;
	@Remark("连接的数据库")
	private String database;
	@Remark("端口")
	private int port;
	
	
	public String getUrl() {
		String str="jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		return String.format(str, host,port,database);
	}
	
	public String getKey() {
		return username+"|"+password+"|"+host+"|"+port+"|"+database;
	}
	
}
