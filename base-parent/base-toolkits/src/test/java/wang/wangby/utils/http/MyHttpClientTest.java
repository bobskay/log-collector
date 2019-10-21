package wang.wangby.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.utils.http.interceptor.HttpRequestInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyHttpClientTest extends TestBase {

    @Test
    public void getString() throws IOException {
            HttpConfig config=new HttpConfig();
            config.setSocketTimeout(100);
            List<HttpRequestInterceptor> list=new ArrayList<>();
            list.add(HttpRequestInterceptor.jsonRequestInterceptor());
            MyHttpClient client=HttpUtil.createClient(config,Config.TESTURL,list);
            String s=client.get("/");
            log.debug(s.replace("\\n","\n"));
    }
}