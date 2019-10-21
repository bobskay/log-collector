package wang.wangby.utils.serialize.json;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.utils.FileUtil;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.json.DictionarySerializer;
import wang.wangby.utils.serialize.json.vo.ComplexObject;
import wang.wangby.utils.serialize.json.vo.MyDictionary;
import wang.wangby.utils.serialize.json.vo.Person;
import wang.wangby.utils.threadpool.EventFuture;
import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolEvent;
import wang.wangby.utils.threadpool.ThreadPoolFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class FastJsonImplTest extends TestBase {

    public static int TEST_SIZE = 10;//测试时创建json数量

    @Before
    public void init() {
        //LogLevel.trace(FastJsonImpl.class);
    }

    @Test
    public void toJs() {
        //字典类使用DictionarySerializer序列化,这里不能用interface
        SerializeConfig.globalInstance.put(MyDictionary.class, DictionarySerializer.instance);
        ParserConfig.getGlobalInstance().putDeserializer(MyDictionary.class, DictionarySerializer.instance);
        ComplexObject object = new ComplexObject();
        String js = jsonUtil.toFormatString(object);
        log.debug("\n" + js);
    }

    @Test
    public void toBean() {
        ParserConfig.getGlobalInstance().putDeserializer(MyDictionary.class, DictionarySerializer.instance);
        String js = FileUtil.getText(this.getClass(), "/complexObject.json");
        log.debug("原始的js\n" + js);
        ComplexObject o = jsonUtil.toBean(js, ComplexObject.class);
        log.debug("转换后\n" + toJson(o));
    }

    @Test
    public void config() {
        setPretty();
        ComplexObject o = new ComplexObject();
        log.debug(jsonUtil.toString(o));
    }

    @Test
    public void customer() {
        SerializeConfig.globalInstance.put(Person.class, new PersonSerializer());
        Person p = new Person();
        p.setName("namename");
        p.setComment("cmtcmt");
        p.setId(111111111L);
        p.setBirthday(new Date());
        char[] pch = personChar;
        System.out.println(new String(pch));
        String js = jsonUtil.toString(p);
        log.debug("[" + js + "]");
    }

    @Test
    //大对象性能测试
    public void large() {
        ComplexObject complexObject = new ComplexObject();
        complexObject.setChild(new ArrayList<>());
        for (int i = 0; i < TEST_SIZE; i++) {
            addChildren(complexObject);
        }
        String js = jsonUtil.toString(complexObject);
        log.debug("js长度:" + js.getBytes().length / 1024 + "(k)");
        for (int i = 0; i < 50; i++) {
            String s = $(() -> jsonUtil.toString(complexObject));
        }
        log.debug("------------custom Serializer------------");
        SerializeConfig.globalInstance.put(Person.class, new PersonSerializer());
        for (int i = 0; i < 50; i++) {
            String s = $(() -> jsonUtil.toString(complexObject));
        }
        log.debug("----------append--------------");
        for (int i = 0; i < 50; i++) {
            String s = $(() -> toJs(complexObject));
        }
    }

    private String toJs(ComplexObject complexObject) {
        List<Person> list = complexObject.getChild();
        StringBuilder sb = new StringBuilder();
        complexObject.setChild(null);
        String js = jsonUtil.toString(complexObject);
        sb.append(js);

        EventFuture sb1 = pool.call(new ThreadPoolEvent() {
            @Override
            public Object call() throws Exception {
                StringBuilder sb1 = new StringBuilder();
                for (int i = 0; i < TEST_SIZE / 3; i++) {
                    appendPersion(sb1, list.get(i));
                }
                return sb;
            }
        });

        EventFuture sb2 = pool.call(new ThreadPoolEvent() {
            @Override
            public Object call() throws Exception {
                StringBuilder sb1 = new StringBuilder();
                for (int i = 30000; i < TEST_SIZE / 3; i++) {
                    appendPersion(sb1, list.get(i));
                }
                return sb;
            }
        });

        EventFuture sb3 = pool.call(new ThreadPoolEvent() {
            @Override
            public Object call() throws Exception {
                StringBuilder sb1 = new StringBuilder();
                for (int i = TEST_SIZE - TEST_SIZE / 3 - TEST_SIZE / 3; i < list.size(); i++) {
                    appendPersion(sb1, list.get(i));
                }
                return sb;
            }
        });
        sb.append(sb1.await());
        sb.append(sb2.await());
        sb.append(sb3.await());

        complexObject.setChild(list);
        return sb.toString();
    }

    private void addChildren(ComplexObject complexObject) {
        long idx = complexObject.getChild().size();
        Person p = new Person();
        p.setId(idx);
        p.setName("abcde");
        p.setComment("aaaabbbcccedddeeefffggg" + idx);
        p.setBirthday(new Date());
        complexObject.getChild().add(p);
    }

    static ThreadPool pool = ThreadPoolFactory.newPool("testJosn", 10);

    //自定义persion的序列化方式
    public static class PersonSerializer implements ObjectSerializer {
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
                return;
            }
            char[] chars = Arrays.copyOf(personChar, personChar.length);
            Person p = (Person) object;

            char[] id = (p.getId() + "").toCharArray();
            char[] name = (p.getName()).toCharArray();
            char[] cmt = (p.getComment()).toCharArray();
            char[] bir = (p.getBirthday().getTime() + "").toCharArray();
            for (int i = idIdx; i < idIdx + id.length; i++) {
                chars[i] = id[i - idIdx];
            }
            for (int i = nameIdx; i < nameIdx + name.length; i++) {
                chars[i] = name[i - nameIdx];
            }
            for (int i = commentIdx; i < commentIdx + cmt.length; i++) {
                chars[i] = cmt[i - commentIdx];
            }
            for (int i = birthIdx; i < birthIdx + bir.length; i++) {
                chars[i] = bir[i - birthIdx];
            }
            out.write(chars);
        }
    }

    static int idIdx = 0;
    static int nameIdx = 0;
    static int commentIdx = 0;
    static int dateIdx = 0;
    static int birthIdx = 0;
    static char[] personChar = createPersonChar();

    public static char[] createPersonChar() {
        Person p = new Person();
        p.setComment(StringUtil.createString(50, " "));
        p.setName(StringUtil.createString(20, " "));
        String id = StringUtil.createString(10, " ");
        String birth = StringUtil.createString(15, " ");
        StringBuilder sb = new StringBuilder();
        sb.append("{id:");
        idIdx = sb.toString().toCharArray().length;
        sb.append(id).append(",");
        sb.append("name:");
        nameIdx = sb.toString().toCharArray().length;
        sb.append(p.getName());
        sb.append("comment:");
        commentIdx = sb.toString().length();
        sb.append(p.getComment());
        sb.append(",birthday:");
        birthIdx = sb.toString().toCharArray().length;
        sb.append(birth);
        sb.append("}");
        return sb.toString().toCharArray();
    }

    public static void appendPersion(StringBuilder sb, Person p) {
        char[] chas = Arrays.copyOf(personChar, personChar.length);
        char[] name = p.getName().toCharArray();
        for (int i = 5; i < name.length; i++) {
            chas[i] = name[i - 5];
        }
        char[] comment = p.getComment().toCharArray();
        for (int i = 20; i < comment.length; i++) {
            chas[i] = comment[i - 20];
        }
        sb.append(new String(chas));
    }

}
