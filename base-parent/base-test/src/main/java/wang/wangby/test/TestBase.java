package wang.wangby.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import wang.wangby.test.assertutils.MyAssert;
import wang.wangby.utils.*;
import wang.wangby.utils.json.FastJsonImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.equalTo;

public class TestBase {
    protected Logger log= LoggerFactory.getLogger(this.getClass());

    protected  static JsonUtil jsonUtil;

    static{
        MDC.put("requestId", "test");
        jsonUtil=new FastJsonImpl();
    }

    //如果覆盖了这个方法就不会自动执行了,需要手动执行
    @Before
    public void initJs() {
        FastJsonImpl.initGlobalConfig(null, null);
        jsonUtil = JsonUtil.getInstance();
    }

    // 判断值是否和某个文件的内容一致,判断是会行判断,并且每行会trim
    public void equalToFile(String real, String expectedFilePath) {
        equalToFile(real, expectedFilePath, true);
    }

    public void equalToFile(String actual, String expectedFilePath, boolean compareByLine) {
        String expected = FileUtil.getText(this.getClass(), expectedFilePath);
        if (!compareByLine) {
            Assert.assertThat(actual, equalTo(expected));
        } else {
            equalToLines(actual, expected, true);
        }
    }

    public void autowiredAll() throws Exception {
        List<Class> clazz = new ArrayList();
        for (Field f : FieldUtils.getAllFields(this.getClass())) {
            f.setAccessible(true);
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            Object value = f.get(this);
            if (value == null) {
                clazz.add(f.getType());
            }
        }
        autowired(clazz.toArray(new Class[]{}));

    }

    public  void assertNotEmpty(Object o) {
        LogUtil.debugCaller("开始判断对象是否为空");
        if (StringUtil.isEmpty(o)) {
            throw new RuntimeException("对象为空,判断失败:" + o);
        } else if (o instanceof Collection) {
            Collection c = (Collection) o;
            if (c.size() == 0) {
                throw new RuntimeException("集合元素个数为0" + o.getClass());
            }
        }
        log.debug("\n" + toJson(o));
    }

    // 按行比较,头尾trim,每行trim
    public void equalToLines(String actual, String expected, boolean trim) {
        if (trim) {
            actual = actual.trim();
            expected = expected.trim();
        }
        String[] actuals = actual.split("\n");
        String[] expecteds = expected.split("\n");
        if (actuals.length != expecteds.length) {
            throw new RuntimeException("实际值有" + actuals.length + "行,期望值是" + expecteds.length + "行");
        }
        for (int i = 0; i < actuals.length; i++) {
            if (trim) {
                actuals[i] = actuals[i].trim();
                expecteds[i] = expecteds[i].trim();
            }
            Assert.assertThat(i + ": " + actuals[i].trim(), equalTo(i + ": " + expecteds[i].trim()));
        }
    }

    public static String toJson(Object obj) {
        return toJson(obj, false);
    }

    public static String toJson(Object obj, boolean showNull) {
        if (showNull) {
            return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat);
        }
        return JSON.toJSONString(obj, SerializerFeature.PrettyFormat);
    }

    @SneakyThrows
    public void setField(Object o, Object value) {
        setField(o, value, true);
    }

    /**
     * 设置字段值,
     *
     * @param o      要赋值的对象
     * @param value  要设置的值
     * @param ignore 是否忽略非空的,当为true并且字段已经有值了就会跳过set
     */
    public void setField(Object o, Object value, boolean ignore) throws Exception {
        for (Field f : FieldUtils.getAllFields(o.getClass())) {
            f.setAccessible(true);
            if (ClassUtil.isInstance(value.getClass(), f.getType())) {
                if (ignore) {
                    Object ori = f.get(o);
                    if (ori != null) {
                        return;
                    }
                }
                f.set(o, value);
                //log.debug("自动填充" + f.getDeclaringClass().getSimpleName() + "." + f.getName() + "=" + value);
                return;
            }
        }
    }

    // 根据传入的类自动填充当前测试类的属性
    @SneakyThrows
    public void autowired(Class... cls) {
        List all = new ArrayList();
        for (Field f : FieldUtils.getAllFields(this.getClass())) {
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            f.setAccessible(true);
            Object o = f.get(this);
            if (o != null) {
                all.add(o);
            }
        }

        // 全部new出来
        for (Class clazz : cls) {
            Object o = ClassUtil.newInstance(clazz);
            all.add(o);
        }

        // 根据类型填充
        for (Object o : all) {
            autowired(o, all);
        }

        autowired(this, all);

    }

    public <T> void assertException(Consumer<T> fun) {
        Exception ex = null;
        try {
            fun.accept(null);
        } catch (Exception e) {
            log.debug("确认异常:" + e.getMessage());
            ex = e;
        }
        if (ex == null) {
            throw new RuntimeException("未出现异常");
        }
    }

    public void autowired(Object o, List values) throws Exception {
        for (Object value : values) {
            setField(o, value, true);
        }
    }

    public void stringEqual(String result, String expect) {
        MyAssert.stringEqual(result,expect,new Exception("验证字符串相等失败"));
    }

    //将json的输出格式设置为格式化的
    public void setPretty() {
        JSON.DEFAULT_GENERATE_FEATURE = JSON.DEFAULT_GENERATE_FEATURE | SerializerFeature.PrettyFormat.mask;
    }

    //每秒执行一次,直到返回false
    public void perSecond(Callable<Boolean> callable, int max) throws InterruptedException {
        CountDownLatch down = new CountDownLatch(1);
        LongAdder add = new LongAdder();
        new Thread(() -> {
            while (true) {
                try {
                    add.add(1);
                    int i = max - add.intValue();
                    log.debug("准备执行第" + add.intValue() + "次");
                    Boolean end = !callable.call();
                    Thread.sleep(1000);
                    if (i == 0 || end) {
                        break;
                    }
                } catch (Exception e) {
                    log.debug("指定周期任务出错", e);
                    break;
                }
            }
            down.countDown();

        }).start();
        down.await();
    }

    public void perSecond(Callable<Boolean> callable) throws InterruptedException {
        perSecond(callable, 10);
    }

    //执行某个方法并计算耗时
    public <T> T $(Callable<T> callable)   {
        String caller=LogUtil.getCalller();
        long begin = System.nanoTime();
        try{
            T result= callable.call();
            System.out.println(caller+": "+getTimes(begin));
            return result;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private String getTimes(long begin){
        double times = System.nanoTime() - begin;
        if (times > 1000 * 1000 * 1000) {
            return StringUtil.round(times / 1000 / 1000 / 1000) + "(s)";
        }
        if (times > 1000) {
            return StringUtil.round(times / 1000 / 1000) + "(ms)";
        }
        return StringUtil.round(times / 1000 / 1000) + "(ns)";
    }
}
