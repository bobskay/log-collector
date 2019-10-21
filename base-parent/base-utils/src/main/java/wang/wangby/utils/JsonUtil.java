package wang.wangby.utils;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.json.FastJsonImpl;

import java.util.HashMap;
import java.util.Map;

@Slf4j
abstract public class JsonUtil {
    private static JsonUtil jsonUtil=new FastJsonImpl();

    //调用此方法前必须先初始化对象,实现类在my-serialize包
    public static JsonUtil getInstance() {
        return jsonUtil;
    }

    synchronized public static void init(JsonUtil jsonUtil) {
        if (JsonUtil.jsonUtil != null) {
            if (JsonUtil.jsonUtil != jsonUtil) {
                log.info("默认的jsonUtil从{}替换为{}", JsonUtil.jsonUtil, jsonUtil);
            }
        }
        JsonUtil.jsonUtil = jsonUtil;
    }

    //将对象转为json
    abstract public String toString(Object obj);

    //将json转为java对象
    abstract public <T> T toBean(String str, Class<T> t);

    //将对象转为json并格式化
    abstract public String toFormatString(Object obj);

    //自定义的类型,因为反序列化的时候需要知道类名,所以将数据转为{className:{}}的型是
    public String customJs(Object obj) {
        Map map = new HashMap();
        map.put(obj.getClass().getName(), obj);
        return toString(map);
    }
    public String toString(String className, Object obj) {
        Map map = new HashMap();
        map.put(className, obj);
        return toString(map);
    }


    //js的格式为,所以直接直接从里面提取出className
    public Object parseCustomerJs(String formatedJs) {
        Map map = toBean(formatedJs, HashMap.class);
        String name = map.keySet().iterator().next() + "";
        try {
            Class clazz = Class.forName(name);
            return toBean(map.values().iterator().next() + "", clazz);
        } catch (Exception ex) {
            throw new RuntimeException("无法自动转为java对象" + formatedJs+":"+ex.getMessage());
        }
    }

}
