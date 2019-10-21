package wang.wangby.utils.template.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import wang.wangby.utils.template.SimpleObject;
import wang.wangby.utils.template.SimpleObjectConvertor;
import wang.wangby.utils.template.TemplateUtil;
import wang.wangby.utils.template.TrimAllDirective;

import java.util.Properties;

@Slf4j
public class VelocityConfig {

	public TemplateUtil templateUtil(VelocityProperties velocityProperties, SimpleObjectConvertor convertor) {
		VelocityEngine velocityEngine = new VelocityEngine();
		Properties p = new Properties();
		p.setProperty("file.resource.loader.class", ClasspathResourceLoader.class.getName());
		p.setProperty("userdirective", TrimAllDirective.class.getName());
		p.setProperty("input.encoding", velocityProperties.getEncoding());
		p.setProperty("output.encoding", velocityProperties.getEncoding());
		p.setProperty("runtime.log", velocityProperties.getLog());
		velocityEngine.init(p);
		TemplateUtil util=new TemplateUtil(velocityEngine, velocityProperties.getRoot());
		log.info("veclocity自动配置完成,配置信息:{}",velocityProperties);

		SimpleObject.init(convertor);
		return util;
	}

}
