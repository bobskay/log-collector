package wang.wangby.codecretor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.autoconfigure.dao.MapperCreator;
import wang.wangby.dao.BookDaoTest;

@Slf4j
public class MapperCreatorTest {

    @Test
    public void getMapperXml() throws Exception {
        MapperCreator creator=MapperCreator.getDefault();
        String xml=creator.getMapperXml(BookDaoTest.class);
        log.debug("生成映射文件:{}",xml);
    }
}