package wang.wangby.web.config;

import lombok.Data;
import wang.wangby.annotation.Remark;

import java.util.*;

@Data
public class IndexPageProperties {
    @Remark("首页要额外导入的js文件")
    private Set<String> js;
    @Remark("标题")
    private String title;
    @Remark("首页实际内容")
    private String indexContentUrl;
    @Remark("图标")
    private Map<String,String> icon=new HashMap<>();
    @Remark("要忽略的菜单")
    private List<String> ignoreMenus=new ArrayList<>();
}
