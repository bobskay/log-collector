package wang.wangby.logmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import wang.wangby.config.DaoAutoConfiguration;
import wang.wangby.web.config.MyFilterAutoConfiguration;
import wang.wangby.web.config.WebAutoConfiguration;
import wang.wangby.web.tools.controller.config.ToolsAutoConfiguration;

import java.io.IOException;

@SpringBootApplication
@Import({DaoAutoConfiguration.class,WebAutoConfiguration.class,  ToolsAutoConfiguration.class})
public class LogManagerApp {
    public static void main(String[] args){
        ConfigurableApplicationContext c= SpringApplication.run(LogManagerApp.class, args);
    }
}
