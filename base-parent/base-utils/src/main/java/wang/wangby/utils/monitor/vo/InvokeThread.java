package wang.wangby.utils.monitor.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Data
public class InvokeThread {
    private String name;//本次调用名称
    private InvokeMethod root;
    private InvokeMethod current;//当前方法
    private int count;//一共调用的方法个数,调用方需要根据这个数字判断是否继续

    public InvokeThread(String name) {
        this.name = name;
        root = new InvokeMethod(null, name, 0);
        count = 0;
        current = root;
    }

    public void begin(String methodName) {
        count++;
        current = current.add(methodName);
    }

    public void end(String methodName) {
        current = current.remove(methodName);
    }

    //遍历统计结果
    public void interator(Consumer<InvokeMethod> consumer) {
        doIterator(root, consumer);
    }

    public void doIterator(InvokeMethod parent, Consumer<InvokeMethod> consumer) {
        consumer.accept(parent);
        List run=new ArrayList(parent.getRunning());
        for (Iterator<InvokeMethod> it=run.iterator();it.hasNext();) {
            doIterator(it.next(), consumer);
        }
        List finish=new ArrayList(parent.getFinish());
        for (Iterator<InvokeMethod> it=finish.iterator();it.hasNext();) {
            doIterator(it.next(), consumer);
        }
    }

    public void finish(){
        root.setEnd(System.nanoTime());
    }
}
