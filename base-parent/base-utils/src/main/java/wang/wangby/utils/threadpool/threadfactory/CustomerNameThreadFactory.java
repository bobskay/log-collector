package wang.wangby.utils.threadpool.threadfactory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CustomerNameThreadFactory implements ThreadFactory {

    private String name;
    private AtomicInteger count;

    //只能通过ThreadFactoryProvider.get获得,不能自己手动创建
    CustomerNameThreadFactory(String name) {
        this.name = name;
        count = new AtomicInteger(0);
    }

    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(name + count.incrementAndGet());
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                log.error(t.getName(), e);
            }
        });
        return thread;
    }
}
