package wang.wangby.web.controller.utils;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;
import wang.wangby.test.TestBase;
import wang.wangby.web.controller.BaseController;
import wang.wangby.web.model.MenuInfo;

import java.util.ArrayList;
import java.util.List;

public class ControllerMenuParserTest extends TestBase {
    @Test
    public void createMenus() throws Exception {
        List list=new ArrayList();
        BaseController c=new AutoMenuController();
        c.afterPropertiesSet();
        list.add(c);
        List<MenuInfo> menus= ControllerMenuParser.createMenus(list);
        for(MenuInfo view:menus){
            log.debug(view+"");
        }
    }

    @RequestMapping("/autoMenu")
    public static class AutoMenuController extends BaseController{

        @Menu
        @RequestMapping("/hello")
        @Remark("自动菜单测试")
        public void index(){}

        @Menu("子菜单")
        @RequestMapping("/subMenu")
        public void subMenu(){}
    }

}