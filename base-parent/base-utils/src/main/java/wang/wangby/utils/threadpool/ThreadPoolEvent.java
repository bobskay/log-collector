package wang.wangby.utils.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
abstract  public class ThreadPoolEvent<T> implements Runnable {

    abstract public T call() throws Exception;

    private ThreadPool threadPool;//执行该事件的线程池,在准备执行的时候放进来
    private Consumer<T> eventCallBack;

    public void run(){
        try{
            T t=this.call();
            if(eventCallBack!=null){
                eventCallBack.accept(t);
            }
        }catch(Throwable e){
            this.error(e);
        }
    }

    //在执行前由threadpool调用
    public void prepare(ThreadPool poolname,Consumer<T> eventCallBack){
        this.threadPool=poolname;
        this.eventCallBack=eventCallBack;
    }

    //执行事件出错
    public void error(Throwable e){
        threadPool.error(this,e);
    }

    public void rejected() {
        log.warn(threadPool.getName()+" "+this+" rejected 线程池满了,忽略当前事件");
    }
}
