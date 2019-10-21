package wang.wangby.utils.threadpool.batch;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.threadpool.ThreadPoolEvent;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

//批量执行任务
@Slf4j
public class BatchEvent extends ThreadPoolEvent {

    private List<ThreadPoolEvent> events;

    public static BatchEvent repeat(ThreadPoolEvent event, int count) {
        List list = new ArrayList();
        for (int i = 0; i < count; i++) {
            list.add(event);
        }
        return new BatchEvent(list);
    }

    public BatchEvent(List<ThreadPoolEvent> events) {
        this.events = events;
    }

    @Override
    public Object call() throws Exception {
        BatchResult result = new BatchResult();
        result.setCreateTime(new Date());
        result.setCreate(System.nanoTime());

        result.setCount(events.size());
        Queue<EachResult> invokeResult = new LinkedBlockingQueue<>(events.size());

        CountDownLatch taskLatch = new CountDownLatch(1);//任务同步锁,保证任务同时执行
        CountDownLatch responseLatch = new CountDownLatch(events.size());//保证任务执行完毕再退出
        for (int i = 0; i < events.size(); i++) {
            Thread thread = createThread(taskLatch, responseLatch, events.get(i), invokeResult);
            thread.start();
        }
        result.setRunBegin(System.nanoTime());
        taskLatch.countDown();//开始执行
        responseLatch.await();//等待执行完毕
        result.setRunEnd(System.nanoTime());
        result.setEachResults(invokeResult);
        result.setRunEnd(System.nanoTime());
        return result;
    }

    private Thread createThread(CountDownLatch taskLatch, CountDownLatch responseLatch, ThreadPoolEvent event, Queue<EachResult> invokeResult) {
        return new Thread(() -> {
            EachResult each = new EachResult();
            each.setBegin(System.nanoTime());
            try {
                taskLatch.await();
                each.setRunTime(new Date());
                each.setInvokeBegin(System.nanoTime());
                try {
                    Object o = event.call();
                    each.setResult(o);
                    each.setInvokeEnd(System.nanoTime());
                } catch (Exception ex) {
                    each.setInvokeEnd(System.nanoTime());
                    each.setResult(ex);
                }
                invokeResult.add(each);
                each.setEnd(System.nanoTime());
            } catch (InterruptedException e) {
                log.error("批量任务被强行中断", e);
                each.setEnd(System.nanoTime());
                each.setResult(e);
            } finally {
                responseLatch.countDown();
            }
        });
    }

}
