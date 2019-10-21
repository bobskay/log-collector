package wang.wangby.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.HtmlUtils;
import wang.wangby.utils.JsonUtil;
import wang.wangby.utils.json.FastJsonImpl;
import wang.wangby.utils.template.SimpleObjectConvertor;
import wang.wangby.utils.template.TemplateUtil;
import wang.wangby.utils.template.config.VelocityConfig;
import wang.wangby.utils.template.config.VelocityProperties;

@Configuration
public class BaseConfig {

    @ConfigurationProperties(prefix = "my.velocity")
    @Bean
    public VelocityProperties velocityProperties(){
        return new VelocityProperties();
    }

    @Bean
    public JsonUtil fastJson() {
        FastJsonImpl.initGlobalConfig(null,null);
        return new FastJsonImpl();
    }

    @Bean
    public TemplateUtil templateUtil(VelocityProperties velocityProperties,JsonUtil jsonUtil) {
        return new VelocityConfig().templateUtil(velocityProperties,  new SimpleObjectConvertor() {
            //将对象转为json
            public String toJson(Object target) {
                return jsonUtil.toString(target);
            }
            //过滤html字符
            public String htmlEscape(String str) {
                return HtmlUtils.htmlEscape(str);
            }
        });
    }

}
