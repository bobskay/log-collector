package wang.wangby.utils.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CacheMap<K,V> extends ConcurrentHashMap<K,V> {

    //获取对象,如果没有就新建一个
    public V get(K key,Supplier<V> supplier) {
       V v=this.get(key);
       if(v!=null){
           return v;
       }
        synchronized (this){
            v=this.get(key);
            if(v!=null){
                return v;
            }
            v=supplier.get();
            put(key,v);
            return v;
        }
    }
}
