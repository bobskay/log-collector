package wang.wangby.web.config;

import lombok.Cleanup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import wang.wangby.test.assertutils.MyAssert;
import wang.wangby.utils.http.HttpUtil;

import java.io.IOException;

@SpringBootApplication
@Import(WebAutoConfiguration.class)
public class BaseWebAutoConfigurationTest {

    public static void main(String[] args) throws IOException {
        @Cleanup
        ConfigurableApplicationContext c= SpringApplication.run(BaseWebAutoConfigurationTest.class, args);
        String hello= HttpUtil.get("http://127.0.0.1:8080/test/hello");
        MyAssert.equalTo(hello,"hello world");
    }

}
