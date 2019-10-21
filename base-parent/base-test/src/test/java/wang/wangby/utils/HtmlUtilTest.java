package wang.wangby.utils;

import org.junit.Test;
import wang.wangby.test.assertutils.MyAssert;

public class HtmlUtilTest {

    @Test
    public void test(){
        String html="<html>hello</html>";
        MyAssert.notEmpty(HtmlUtil.htmlEscape(html));
    }
}
