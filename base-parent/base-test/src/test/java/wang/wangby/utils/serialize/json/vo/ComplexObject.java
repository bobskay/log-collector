package wang.wangby.utils.serialize.json.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ComplexObject {
    private String str="this is a String";
    private Integer integer=1;
    private Double doubleValue=1.0;
    private Long longValue=1234567890L;
    @JSONField(format = "yyyy-MM-dd hh:mm:ss")
    private Date jsonDate=new Date();
    private Date myDate=new Date();
    private Map map=new HashMap();
    private String nullString;
    private List<Person> child;
    private MyEnum myEnum=MyEnum.enum1;
    private MyDictionary myDictionary=MyDictionary.dic1;

}
