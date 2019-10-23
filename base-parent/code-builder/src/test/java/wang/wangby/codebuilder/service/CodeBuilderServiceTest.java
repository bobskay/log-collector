package wang.wangby.codebuilder.service;

import org.junit.Test;
import wang.wangby.codebuilder.controller.vo.CodeConfig;
import wang.wangby.utils.template.TemplateUtil;

import static org.junit.Assert.*;

public class CodeBuilderServiceTest {
    public static void main(String[] args) {
        CodeBuilderServiceTest test=new CodeBuilderServiceTest();
       test.createDubbo();
      //  test.createSimple();
    }

    public void createDubbo(){
        CodeConfig config = getCodeConfig();
        CodeBuilderService service=new CodeBuilderService();
        service.templateUtil= TemplateUtil.getDefault();
        String template="E:\\2019\\log-collector\\base-parent\\code-builder\\src\\main\\resources\\codeTemplate\\dubbo";
        String out="E:\\2019\\log-collector\\examples\\dubbo";
        service.createProject(config,template,out);
    }

    public void createSimple(){
        CodeConfig config = getCodeConfig();
        CodeBuilderService service=new CodeBuilderService();
        service.templateUtil= TemplateUtil.getDefault();
        String template="E:\\2019\\log-collector\\base-parent\\code-builder\\src\\main\\resources\\codeTemplate\\simple";
        String out="E:\\2019\\log-collector\\examples\\hello";
        service.createProject(config,template,out);
    }


    private CodeConfig getCodeConfig() {
        CodeConfig config=new CodeConfig();
        config.setSql("CREATE TABLE `t_bookInfo` (\n" +
                "  `bookId` bigint(20) NOT NULL COMMENT '图书id',\n" +
                "  `bookName` varchar(64) DEFAULT NULL COMMENT '名称',\n" +
                "  `publication` datetime DEFAULT NULL COMMENT '发行日期',\n" +
                "  `price` int(11) DEFAULT NULL COMMENT '价格',\n" +
                "  `isbn` varchar(64) DEFAULT NULL COMMENT 'isbn',\n" +
                "  `valid` tinyint(4) DEFAULT NULL COMMENT '是否有效',\n" +
                "  `remain` int(11) DEFAULT NULL COMMENT '剩余',\n" +
                "  `createTime` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`bookId`),\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
        config.setAppName("hello");
        config.setWorkdir("E:\\2019\\log-collector\\examples");
        config.setPackageName("wang.wangby.hello");
        CodeConfig.Datasource ds=new CodeConfig.Datasource();
        ds.setPassword("123456");
        ds.setUrl("jdbc:mysql://192.168.2.11:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false");
        ds.setUsername("root");
        config.setDatasource(ds);
        config.setMenuName("hellow world");

        CodeConfig.DubboConfig dubboConfig=new CodeConfig.DubboConfig();
        dubboConfig.setRegistryAddress("zookeeper://192.168.2.31:2181");
        config.setDubbo(dubboConfig);
        return config;
    }

}