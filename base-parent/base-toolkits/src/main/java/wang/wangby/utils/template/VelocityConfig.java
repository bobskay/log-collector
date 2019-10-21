package wang.wangby.utils.template;

import lombok.Data;

@Data
public class VelocityConfig {
	private String encoding="UTF-8";
	private String root="/templates";//模板文件保存的根路径
	private String log="/opt/logs/velocity.log";
}
