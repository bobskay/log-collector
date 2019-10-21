package wang.wangby.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;

@Slf4j
public class FixSizeListTest extends TestBase {

    @Test
    public void test(){
        FixSizeList list=new FixSizeList(5);
        for(int i=0;i<10;i++){
            log.debug(list.getData()+"");
            list.add(i+"");
        }
    }
}
