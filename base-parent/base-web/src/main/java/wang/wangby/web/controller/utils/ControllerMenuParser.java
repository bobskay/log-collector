package wang.wangby.web.controller.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;
import wang.wangby.utils.ClassUtil;
import wang.wangby.utils.CollectionUtil;
import wang.wangby.utils.StringUtil;
import wang.wangby.web.controller.BaseController;
import wang.wangby.web.controller.CustomMenuController;
import wang.wangby.web.model.MenuInfo;
import wang.wangby.web.model.vo.ControllerMethod;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class ControllerMenuParser {

    public static List<MenuInfo> createMenus(Collection<BaseController> baseControllers) {
        List<BaseController> controllers = new ArrayList<>(baseControllers);
        CollectionUtil.sort(controllers, BaseController::order);

        List<MenuInfo> views = new ArrayList<>();
        for (BaseController controller : controllers) {
            if (controller instanceof CustomMenuController) {
                ((CustomMenuController) controller).addMenu(views);
                continue;
            }

            String className = controller.getPath();
            className = className.replaceAll("/", "");

            List<ControllerMethod> controllerMethods = new ArrayList();
            ControllerMethod index = null;
            for (Method method : ClassUtil.getMethodsByAnnotation(controller.getClass(), Menu.class)) {
                ControllerMethod md = createMethod(method);
                controllerMethods.add(md);
                if (method.getName().equalsIgnoreCase("index")) {
                    index = md;
                }
            }
            //如果没有方法标记menu直接返回
            if (controllerMethods.size() == 0) {
                log.debug("类上未标记任何菜单,跳过:" + controller.getClass().getName());
                continue;
            }

            Menu menu = ClassUtil.getAnnotation(controller.getClass(), Menu.class);
            MenuInfo dir = null;
            //如果类上标记了menu或者菜单数>1就建一个目录
            if (menu != null || controllerMethods.size() > 1) {
                String icon=menu==null?"":menu.icon();
                dir = MenuInfo.newInstance(className, getMenuByController(controller.getClass()), null,icon);
                dir.setNodes(new ArrayList());
            }
            //只有一个方法加在主菜单上
            if (controllerMethods.size() == 1) {
                ControllerMethod md = controllerMethods.get(0);
                if (dir != null) {
                    dir.getNodes().add(MenuInfo.newInstance(className, showName(md), url(md),icon(md)));
                    views.add(dir);
                } else {
                    views.add(MenuInfo.newInstance(className, showName(md), url(md),icon(md)));
                }
                continue;
            }


            //在多个菜单情况下还有1个叫index,就以这个index作为目录
            if (index != null) {
                log.debug("找到首页:" + name(index) + "=" + url(index));
                dir = MenuInfo.newInstance(className, showName(index), url(index),icon(index));
                dir.setNodes(new ArrayList<>());
            }
            for (ControllerMethod md : controllerMethods) {
                if (md == index) {
                    continue;
                }
                log.debug("---添加菜单:" + showName(md) + "=" + url(md));
                dir.addNode(name(md), showName(md), url(md),icon(md));
            }
            views.add(dir);
        }

        //将同名称的目录合并
        List<MenuInfo> list = mergeDir(views);

        return list;
    }

    private static String icon(ControllerMethod md) {
        return md.getMenu().icon();
    }

    private static List<MenuInfo> mergeDir(List<MenuInfo> views) {
        Map<String, MenuInfo> dir = new HashMap<>();
        List<MenuInfo> result = new ArrayList<>();
        for (MenuInfo view : views) {
            MenuInfo old = dir.get(view.getText());
            if (old == null) {
                dir.put(view.getText(), view);
                result.add(view);
                continue;
            }
            //开始合并
            old.getNodes().addAll(view.getNodes());
            if (StringUtil.isNotEmpty(view.getUrl())) {
                if (StringUtil.isEmpty(old.getUrl())) {
                    old.setUrl(view.getUrl());
                } else {
                    log.warn("菜单名称冲突,相同名称:{},不同的{}!={}", view.getText(), view.getUrl(), old.getUrl());
                }
            }
        }
        return result;
    }

    //名称,调用菜单时的标识
    public static String name(ControllerMethod controllerMethod) {
        return controllerMethod.getMethod().getDeclaringClass().getSimpleName() + "_" + controllerMethod.getMethod().getName();
    }

    //显示用菜单时名称
    public static String showName(ControllerMethod controllerMethod) {
        if (controllerMethod.getMenu() != null && StringUtil.isNotEmpty(controllerMethod.getMenu().value())) {
            return controllerMethod.getMenu().value();
        }
        return getRemark(controllerMethod.getRemark(), controllerMethod.getMethod().getName());
    }

    //提取remark注释里逗号前的内容
    public static String getRemark(Remark remark, String defaultName) {
        if (remark == null || StringUtil.isEmpty(remark.value())) {
            return defaultName;
        }
        String value = remark.value();
        int i = value.indexOf(',');
        if (i == -1) {
            return value;
        }
        return value.substring(0, i);
    }

    //通过Controller类获得菜单名称
    private static String getMenuByController(Class clazz) {
        Menu menu= ClassUtil.getAnnotation(clazz, Menu.class);
        if(menu!=null){
            return StringUtil.getFirstBefore(menu.value(),",");
        }
        Remark remark =ClassUtil.getAnnotation(clazz, Remark.class);
        if(remark!=null){
            return StringUtil.getFirstBefore(remark.value(),",");
        }
        String name = clazz.getSimpleName();
        return StringUtil.firstLower(name);
    }

    //方法对应的访问地址
    public static String url(ControllerMethod method) {
        String dir = getPath(method.getClassRequestMapping());
        String name = getPath(method.getRequestMapping());
        if (StringUtil.isEmpty(dir)) {
            return "/" + name;
        }
        return "/" + dir + "/" + name;
    }

    private static String getPath(RequestMapping requestMapping) {
        if (requestMapping == null) {
            return "";
        }
        String[] value = requestMapping.value();
        if (value == null || value.length == 0) {
            return "";
        }
        String path = value[0];
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static ControllerMethod createMethod(Method method) {
        Menu menu = AnnotationUtils.getAnnotation(method, Menu.class);
        ControllerMethod md = new ControllerMethod();
        md.setClassRequestMapping(AnnotationUtils.getAnnotation(method.getDeclaringClass(), RequestMapping.class));
        md.setMethod(method);
        md.setClassRemark(AnnotationUtils.getAnnotation(method.getDeclaringClass(), Remark.class));
        md.setMenu(menu);
        md.setRemark(AnnotationUtils.getAnnotation(method, Remark.class));
        md.setRequestMapping(AnnotationUtils.getAnnotation(method, RequestMapping.class));
        return md;
    }
}
