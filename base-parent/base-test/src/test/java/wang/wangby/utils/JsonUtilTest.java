package wang.wangby.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.utils.serialize.json.vo.Person;

@Slf4j
public class JsonUtilTest extends TestBase {

    @Test
    public void toBean(){
        Person p=new Person();
        p.setName("abc");
        p.setId(1L);
        String js=jsonUtil.toString(p.getClass().getName(),p);
        log.debug(js);

        Object o=jsonUtil.parseCustomerJs(js);
        log.debug(o+"");
    }

    @Test
    public void withoutQuotation(){
        String name=Person.class.getName();
        String js="{'"+name+"':{id:1,name:'abc'}}";
        Object o=jsonUtil.parseCustomerJs(js);
        log.debug(o+"");
    }

}
