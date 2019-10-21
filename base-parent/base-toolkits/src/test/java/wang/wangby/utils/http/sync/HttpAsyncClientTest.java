package wang.wangby.utils.http.sync;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.nio.reactor.IOReactorException;
import org.junit.Test;
import wang.wangby.test.beanfactory.TestBeanFactory;
import wang.wangby.utils.http.Config;
import wang.wangby.utils.http.HttpConfig;
import wang.wangby.utils.threadpool.threadfactory.ThreadFactoryProvider;

import java.util.concurrent.ExecutionException;

@Slf4j
public class HttpAsyncClientTest {

    @Test
    public void doGet() throws IOReactorException, ExecutionException, InterruptedException {
        HttpAsyncClientProvider provider =new HttpAsyncClientProvider(new ThreadFactoryProvider());
        HttpAsyncClient client = provider.newClient(new HttpConfig());
        HttpResponse response = client.doGet(Config.TESTURL);
        log.debug("异步调用结果:"+response);
    }
}