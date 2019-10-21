package wang.wangby.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.test.model.BookInfo;

import javax.xml.bind.JAXBException;

@Slf4j
public class XmlUtilTest extends TestBase {


    @Test
    public void toBean() throws JAXBException {
        String xml = FileUtil.getText(XmlUtilTest.class, "bookInfo.xml");
        BookInfo bk = XmlUtils.toBean(xml, BookInfo.class);
        log.debug(bk.getBookId() + ":" + bk.getPrice());
    }
}
