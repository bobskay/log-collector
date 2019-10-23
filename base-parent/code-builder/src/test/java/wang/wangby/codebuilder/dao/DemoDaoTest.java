package wang.wangby.codebuilder.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.Resource;
import wang.wangby.autoconfigure.dao.DaoConfig;
import wang.wangby.autoconfigure.dao.MapperCreator;
import wang.wangby.codebuilder.model.DemoModel;

import java.util.List;

public class DemoDaoTest {

    @org.junit.Test
    public void getCount() throws Exception {
        DaoConfig config=new DaoConfig();

        DruidDataSource ds=new DruidDataSource();
        ds.setUsername("root");
        ds.setPassword("123456");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://mysql1:3306/test");

        List<Resource> resources=config.getMybatisMapperResource(MapperCreator.getDefault());
        SqlSessionFactory sqlSessionFactory=config.createSqlSessionFactory(ds,resources);
        SqlSession session=sqlSessionFactory.openSession();
        DemoDao test=session.getMapper(DemoDao.class);
        DemoModel demoModel=new DemoModel();
        demoModel.put("tableName","book");
        test.getCount(demoModel);

    }
}