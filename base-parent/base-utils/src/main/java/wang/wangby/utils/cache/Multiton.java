package wang.wangby.utils.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

//多例
abstract public class Multiton<T> {
    private Map<String,T> map=new ConcurrentHashMap();

    public T get(String key){
        T t=map.get(key);
        if(t!=null){
            return t;
        }
        synchronized (this){
            T value=map.get(key);
            if(value!=null){
                return value;
            }
            value=getInner(key);
            map.put(key,value);
            return value;
        }
    }

    abstract protected T getInner(String key);
}
