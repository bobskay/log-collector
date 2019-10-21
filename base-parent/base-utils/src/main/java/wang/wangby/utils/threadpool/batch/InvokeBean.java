package wang.wangby.utils.threadpool.batch;

//统计时,如果InvokeBean的result还是InvokeBean,就取最里面的当做返回值
public interface InvokeBean {
    Long getInvokeBegin();
    Long getInvokeEnd();
    Object getResult();
}
