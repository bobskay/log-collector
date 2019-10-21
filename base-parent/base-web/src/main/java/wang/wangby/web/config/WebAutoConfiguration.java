package wang.wangby.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import wang.wangby.annotation.persistence.Id;
import wang.wangby.utils.IdWorker;
import wang.wangby.web.controller.IndexController;
import wang.wangby.web.controller.MyErrorController;

@Configuration
@Import({MvcAutoconfguration.class,MyFilterAutoConfiguration.class})
@Slf4j
public class WebAutoConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "my.web.index")
    public IndexPageProperties indexPageProperties() {
        return new IndexPageProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public IndexController indexController(IndexPageProperties indexPageProperties){
        log.debug("加载默认首页:"+indexPageProperties);
        return new IndexController();
    }

    @Bean
    public ErrorController myErrorController(){
        return new MyErrorController();
    }


}
