package wang.wangby.wang.wangby.utils;

import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.test.assertutils.MyAssert;
import wang.wangby.utils.StringPicker;

public class StringPickerTest extends TestBase {

    @Test
    public void push(){
        StringPicker picker=new StringPicker("  123 4567");
        String str=picker.next("  ");
        MyAssert.equalTo(str,"");
        str=picker.next("456");
        MyAssert.equalTo(str,"123 ");
        picker.push(str+"456");//还原
        MyAssert.equalTo(picker.nextAll(),"123 4567");
    }
}
