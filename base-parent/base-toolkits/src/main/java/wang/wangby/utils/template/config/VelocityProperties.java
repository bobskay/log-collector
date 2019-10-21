package wang.wangby.utils.template.config;

import lombok.Data;

@Data
public class VelocityProperties {
	private String encoding="UTF-8";
	private String root="/templates";//模板跟路径,相对于classpath
	private String log="/opt/logs/velocity/velocity.log";//velocity日志地址
}
