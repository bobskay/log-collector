package wang.wangby.utils.threadpool.threadfactory;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class ThreadFactoryProviderTest {

    @Test
    public void test() throws InterruptedException {
        ThreadFactoryProvider provider=new ThreadFactoryProvider();
        ThreadFactory factory=provider.get("hello");
        CountDownLatch latch=new CountDownLatch(1);
        CountDownLatch totalLatch=new CountDownLatch(10);
        for(int i=0;i<10;i++){
            Thread thread=factory.newThread(()->{
                try {
                    latch.await();
                    String name=Thread.currentThread().getName();
                    log.debug("线程正在执行;"+name);
                    if("hello1".equals(name)){
                        throw new RuntimeException("测试未捕捉的异常");
                    }
                    log.debug("线程正常结束;"+name);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }finally {
                    totalLatch.countDown();
                }
            });
            thread.start();
            log.debug("创建线程"+thread.getName());
        }
        latch.countDown();
        totalLatch.await();
        Thread thread=factory.newThread(()->{

        });
        log.debug("结束:"+thread.getName());
    }
}
