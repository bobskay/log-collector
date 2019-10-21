package wang.wangby.logmanager.service;

import lombok.Cleanup;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import wang.wangby.config.DaoAutoConfiguration;
import wang.wangby.test.TestBase;
import wang.wangby.web.config.WebAutoConfiguration;

import java.io.IOException;

@SpringBootApplication
@Import({WebAutoConfiguration.class, DaoAutoConfiguration.class})
@MapperScan({"wang.wangby.logmanager"})
public class ServerInfoServiceTest extends TestBase {

    public static void main(String[] args) throws IOException {
        @Cleanup
        ConfigurableApplicationContext c= SpringApplication.run(ServerInfoServiceTest.class, args);
        ServerInfoService serverInfoService=  c.getBean(ServerInfoService.class);
        serverInfoService.get(1L);
    }

}