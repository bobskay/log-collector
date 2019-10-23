package wang.wangby.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import wang.wangby.autoconfigure.dao.DaoConfig;
import wang.wangby.autoconfigure.dao.DaoProperties;
import wang.wangby.autoconfigure.dao.MapperCreator;
import wang.wangby.utils.IdWorker;
import wang.wangby.utils.template.TemplateUtil;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.List;

@Configuration
@Import(BaseConfig.class)
@Slf4j
public class DaoAutoConfiguration {

    @ConfigurationProperties(prefix = "my.dao")
    @Bean
    public DaoProperties daoProperties(){
        return new DaoProperties();
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, DaoProperties daoProperties,TemplateUtil templateUtil) throws Exception {
        log.debug("准备创建SqlSessionFactory,数据源={},系统配置={}",dataSource.getClass().getSimpleName(),daoProperties);
        MapperCreator mapperCreator=new MapperCreator(templateUtil,daoProperties);
        DaoConfig config=new DaoConfig();
        List<Resource> resources=config.getMybatisMapperResource(mapperCreator);
        SqlSessionFactory sqlSessionFactory=config.createSqlSessionFactory(dataSource,resources);
        return sqlSessionFactory;
    }

    @Bean
    public IdWorker idWorker(DaoProperties daoProperties) throws NoSuchFieldException, IllegalAccessException {
        IdWorker idWorker=new IdWorker(daoProperties.getMachineNo());
        Field field=IdWorker.class.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        field.set(IdWorker.class,idWorker);
        log.info("初始化主键生成器成功,当前机器号:"+daoProperties.getMachineNo());
        return idWorker;
    }


}
