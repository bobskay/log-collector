package wang.wangby.utils.monitor;

import wang.wangby.utils.monitor.vo.InvokeThread;


public class MonitorUtil {
    public static int maxInvoke = 10000;
    private static ThreadLocal<InvokeThread> local=new ThreadLocal<>();

    public static void begin(String methodName) {
        InvokeThread thread = local.get();
        //只用显示初始化了,当前线程才开始统计
        if (thread == null) {
            return;
        }
        try {
            //调用堆栈过深不记录了
            if (thread.getCount() >= maxInvoke) {
                System.err.println("too many invoke " + maxInvoke + ",reset");
                thread=init(thread.getName());
            }
            thread.begin(methodName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void end(String methodName) {
        InvokeThread thread = local.get();
        if (thread == null) {
            return;
        }
        try {
            thread.end(methodName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //初始化本线程的统计信息
    public static InvokeThread init(String name) {
        InvokeThread thread=new InvokeThread(name);
        local.set(thread);
        return thread;

    }

    public static InvokeThread  finish() {
        InvokeThread t= local.get();
        t.finish();
        local.remove();
        return t;
    }
}
