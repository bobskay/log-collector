package wang.wangby.logtask;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import wang.wangby.config.DaoAutoConfiguration;
import wang.wangby.logtask.processor.LogProcessor;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.web.config.WebAutoConfiguration;

import java.io.IOException;

@SpringBootApplication
@Import({WebAutoConfiguration.class, DaoAutoConfiguration.class})
@ComponentScan({"wang.wangby.logmanager","wang.wangby.logtask"})
@MapperScan("wang.wangby.logmanager")
public class LogTaskApp {
    public static void main(String[] args) throws IOException {
        args=new String[]{
          "--my.task.taskId=106359162694272000"    ,
          "--my.task.serverName=myos",
          "--server.port=8888"
        };

        ConfigurableApplicationContext c= SpringApplication.run(LogTaskApp.class, args);
    }

    @Bean
    @ConfigurationProperties(prefix = "my.task")
    public TaskConfig taskConfig() {
        return new TaskConfig();
    }


    @Bean
    public LogProcessor logProcessor(){
        return new LogProcessor();
    }

    @Bean
    public ShellClient shellClient(){
        return new ShellClient(60*5,"logTask");
    }
}