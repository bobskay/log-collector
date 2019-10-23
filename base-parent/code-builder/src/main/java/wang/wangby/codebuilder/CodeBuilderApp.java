package wang.wangby.codebuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import wang.wangby.config.DaoAutoConfiguration;
import wang.wangby.web.config.WebAutoConfiguration;
import wang.wangby.web.tools.controller.config.ToolsAutoConfiguration;

import java.io.IOException;

@SpringBootApplication
@Import({WebAutoConfiguration.class, DaoAutoConfiguration.class, ToolsAutoConfiguration.class})
public class CodeBuilderApp {
    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext c= SpringApplication.run(CodeBuilderApp.class, args);
    }

}
