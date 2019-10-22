package wang.wangby.logmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import wang.wangby.config.DaoAutoConfiguration;
import wang.wangby.web.tools.controller.config.ToolsAutoConfiguration;

@Configuration
@Import({DaoAutoConfiguration.class, ToolsAutoConfiguration.class})
public class LogManagerAppConfig {



    @Bean
    @ConfigurationProperties(prefix = "my.task-manager")
    public LogManagerProperties logManagerProperties(){
        return new LogManagerProperties();
    }

}
