package wang.wangby.utils.threadpool;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.exception.Message;
import wang.wangby.utils.LogUtil;
import wang.wangby.utils.cache.CacheMap;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

@Slf4j
public class ThreadPool {
    //执行出错时,记录每种错误发生的次数
    //className,count
    private CacheMap<String, LongAdder> errorInfo = new CacheMap();
    private LongAdder errorCount=new LongAdder();
    // 线程池
    private ThreadPoolExecutor threadPool;
    //超过线程池最大值,失败次数
    private AtomicLong rejected = new AtomicLong();
    @Getter
    private String name;

    public ThreadPool(int corePoolSize, int maximumPoolSize, int keepAliveTime, int queue, String name) {
        this(name, corePoolSize, maximumPoolSize, keepAliveTime, queue);
    }

    public ThreadPool(String name, int corePoolSize, int maximumPoolSize, int keepAliveTime, int queue) {
        this.name = name;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = null;
        if (queue == 0) {
            workQueue = new SynchronousQueue();
        } else {
            workQueue = new LinkedBlockingQueue<Runnable>(queue);
        }

        RejectedExecutionHandler rejectedExecutionHandler = (Runnable run, ThreadPoolExecutor executor) -> {
            rejected.incrementAndGet();
            ThreadPoolEvent r = (ThreadPoolEvent) run;
            r.rejected();
        };

        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new NamedThreadPoolFactory(name), rejectedExecutionHandler);
    }


    //执行系统事件
    public void execute(final Event event) {
        event.prepare(this, null);
        threadPool.execute(event);
    }

    //执行系统事件,可并拿到future对象
    public EventFuture call(final ThreadPoolEvent event) {
        EventFuture future = new EventFuture();
        event.prepare(this, t -> {
            future.setResult(t);
        });
        threadPool.execute(event);
        return future;
    }

    //执行系统事件,自定义会调方法
    public <T> void call(final ThreadPoolEvent<T> event, Consumer<T> callback) {
        event.prepare(this, callback);
        threadPool.execute(event);
    }

    /**
     * 当前线程池执行情况
     */
    public PoolInfo getInfo() {
        return new PoolInfo();
    }

    //事件执行出错
    public void error(ThreadPoolEvent event, Throwable e) {
        e= LogUtil.getCause(e);
        LongAdder adder=errorInfo.get(e.getClass().getSimpleName(),()->new LongAdder());
        adder.increment();
        errorCount.increment();;
        if (e instanceof Message) {
            log.warn(name+"执行系统事件出错" + event + ":" + e.getMessage());
        }else{
            log.error(name+"执行系统事件出错" + event + ":" + e.getMessage(), e);
        }
    }

    public class PoolInfo {
        public int getActiveCount() {
            return threadPool.getActiveCount();
        }

        //已经完成的个数
        public long getCompletedTaskCount() {
            return threadPool.getCompletedTaskCount();
        }

        //曾经最大线程池个数
        public int getLargestPoolSize() {
            return threadPool.getPoolSize();
        }

        public long getTaskCount() {
            return threadPool.getTaskCount();
        }

        //正在排队的个数
        public int getQueueSize() {
            return threadPool.getQueue().size();
        }

        //线程池大小
        public int getPoolSize() {
            return threadPool.getPoolSize();
        }

        //被拒绝个数
        public long getRejected() {
            return rejected.get();
        }

        //异常次数
        public long getError(){
            return errorCount.longValue();
        }
    }

}
