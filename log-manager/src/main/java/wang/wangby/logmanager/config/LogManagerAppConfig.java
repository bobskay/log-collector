package wang.wangby.logmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import wang.wangby.config.DaoAutoConfiguration;
import wang.wangby.utils.shell.ShellClient;

@Configuration
@Import(DaoAutoConfiguration.class)
public class LogManagerAppConfig {

    @Bean
    public ShellClient shellClient(){
       return new ShellClient(60*10,"logManager");
    }

    @Bean
    @ConfigurationProperties(prefix = "my.task-manager")
    public LogManagerProperties logManagerProperties(){
        return new LogManagerProperties();
    }
}
