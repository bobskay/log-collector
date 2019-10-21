package wang.wangby.test.model;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class BookInfoTest {

    @Test
    public void id(){
        BookInfo bookInfo=new BookInfo();
        log.debug("一开始id是空的:"+bookInfo.id());
        bookInfo.id(1234L);
        log.debug("设置后:"+bookInfo.id());
    }
}