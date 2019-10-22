package wang.wangby.web.tools.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import wang.wangby.utils.shell.LongTimeShellPool;
import wang.wangby.utils.shell.ShellClient;

@Configuration
@ComponentScan("wang.wangby.web.tools.controller")
public class ToolsAutoConfiguration {

    @Bean
    public ShellClient shellClient(){
        return new ShellClient(60*10,"logManager");
    }

    @Bean
    public LongTimeShellPool longTimeShellPool(ShellClient shellClient){
        return new LongTimeShellPool(shellClient,10,60);
    }
}
