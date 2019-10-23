package wang.wangby.codebuilder.controller.vo;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.utils.StringUtil;

@Data
public class CodeConfig {
    private String sql;
    @Remark("要生成代码所在包名")
    private String packageName;
    @Remark("主菜单名")
    private String menuName;
    private String hostname;
    private String username;
    private String password;
    private String workdir;
    private Datasource datasource;
    @Remark("模块名")
    private String modelName;
    @Remark("项目名称")
    private String appName;

    private DubboConfig dubbo=new DubboConfig();

    @Data
    public static class Datasource{
        private String username;
        private String password;
        private String url;
    }

    public String getUpModelName(){
        return StringUtil.firstUp(modelName);
    }

    public String getUpAppName(){
        return StringUtil.firstUp(appName);
    }

    public String getPackageDir(){
        return packageName.replace('.','/');
    }


    @Data
    public static class DubboConfig{
        private String registryAddress;
    }
}
