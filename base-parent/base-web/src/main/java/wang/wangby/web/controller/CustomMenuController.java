package wang.wangby.web.controller;

import wang.wangby.web.model.MenuInfo;

import java.util.List;

//自己生成菜单的controller
public interface CustomMenuController {

    /**
     * 添加新菜单，创建菜单的时候会依次遍历controller，如果是CustomMenuController就会调用这个方法加入菜单
     * 其它controller采用默认规则
     * @param menus 菜单列表
     * */
    void addMenu(List<MenuInfo> menus);

}
