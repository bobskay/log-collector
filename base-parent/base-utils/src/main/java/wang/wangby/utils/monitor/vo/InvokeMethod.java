package wang.wangby.utils.monitor.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvokeMethod {
    private List<InvokeMethod> running = new ArrayList<>();
    private List<InvokeMethod> finish = new ArrayList<>();
    private  InvokeMethod parent;
    private long begin;
    private long end;
    private int level;
    private String name;

    public InvokeMethod(InvokeMethod parent, String name, int level) {
        this.name = name;
        this.level = level;
        this.begin = System.nanoTime();
        this.parent = parent;
    }

    public InvokeMethod add(String methodName) {
        if (running.size() != 0) {
            InvokeMethod last = running.get(running.size() - 1);
            last.add(methodName);
            return last;
        }
        InvokeMethod method = new InvokeMethod(this, methodName, this.level + 1);
        running.add(method);
        return this;
    }

    public InvokeMethod remove(String methodName) {
        if (running.size() == 0) {
            if(parent==null){
                System.err.println("ignore remove root:"+methodName);
                return this;
            }
            this.end = System.nanoTime();
            InvokeMethod remove = parent.running.remove(parent.running.size() - 1);
            parent.finish.add(remove);
            return parent;
        }
        InvokeMethod last = running.remove(running.size() - 1);
        last.end = System.nanoTime();
        finish.add(last);
        return this;
    }

    public double times(){
        if(end==0){
            System.err.println("end=0:"+name);
            return 0;
        }
        double d=end-begin;
        return d/1000/1000;
    }
}
