package wang.wangby.autoconfigure.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.core.io.Resource;
import wang.wangby.dao.BookDaoTest;
import wang.wangby.test.TestBase;
import wang.wangby.test.model.BookInfo;

import java.util.List;

public class DaoConfigTest extends TestBase {

    @Test
    public void createSqlSessionFactory() throws Exception {
        DaoConfig config=new DaoConfig();

        DruidDataSource ds=new DruidDataSource();
        ds.setUsername("root");
        ds.setPassword("123456");
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://mysql1:3306/test");

        List<Resource> resources=config.getMybatisMapperResource(MapperCreator.getDefault());
        SqlSessionFactory sqlSessionFactory=config.createSqlSessionFactory(ds,resources);
        SqlSession session=sqlSessionFactory.openSession();
        BookDaoTest test=session.getMapper(BookDaoTest.class);

        BookInfo bookInfo=new BookInfo();
        bookInfo.getExt().put("offset",1);
        bookInfo.getExt().put("limit",10);
        List<BookInfo>  bookInfos=test.select(bookInfo);
        log.debug("找到记录数:{}",bookInfos.size());
    }
}