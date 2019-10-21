package wang.wangby.utils;

import org.junit.Test;
import wang.wangby.test.TestBase;

public class StringUtilTest extends TestBase {


    @Test
    public void getLastBefore(){
        String str="com.a.b.c.eee";
        String tail=".";
        stringEqual("com.a.b.c",StringUtil.getLastBefore(str,tail));
    }

    @Test
    public void toStringArray(){
        String[] str=new String[2];
        str[0]="abc";
        str[1]="bcd";
        stringEqual("[abc,bcd]",StringUtil.toString(str));
        stringEqual("[]",StringUtil.toString(new String[0]));
    }
}
