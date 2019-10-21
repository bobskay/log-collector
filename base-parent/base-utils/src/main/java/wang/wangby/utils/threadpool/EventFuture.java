package wang.wangby.utils.threadpool;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventFuture<T> {
    private T result;
    private CountDownLatch countDownLatch=new CountDownLatch(1);

    public void setResult(T result){
        this.result=result;
        countDownLatch.countDown();
    }

    @SneakyThrows
    public T await() {
        countDownLatch.await();
        return result;
    }

    @SneakyThrows
    /**
     * 等待一段时间,如果还没返回值就直接返回null
     * @param timeout 等待时间,单位毫秒
     * */
    public T waiteMilli(long timeout) {
        countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        return result;
    }
}

