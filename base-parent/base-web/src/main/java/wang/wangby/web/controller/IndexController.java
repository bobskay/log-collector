package wang.wangby.web.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import wang.wangby.model.request.Response;
import wang.wangby.utils.StringUtil;
import wang.wangby.web.config.IndexPageProperties;
import wang.wangby.web.model.MenuInfo;

import java.util.*;

@RestController
public class IndexController extends BaseController implements ApplicationContextAware {
    ApplicationContext applicationContext;
    @Autowired
    IndexPageProperties indexPageProperties;

    @RequestMapping({"/","/index"})
    public String index(String forward) {
        Map map=new HashMap();
        map.put("title",indexPageProperties.getTitle());
        map.put("title",indexPageProperties.getTitle());
        map.put("jsList", indexPageProperties.getJs());
        if(StringUtil.isEmpty(forward)){
            forward=indexPageProperties.getIndexContentUrl();
        }
        map.put("forward", forward);
        map.put("menus",super.getDefaultMenu(indexPageProperties));
        return $("/index",map);
    }

    @RequestMapping("/menus")
    public Response<List<MenuInfo>> menus() {
        List list=super.getDefaultMenu(indexPageProperties);
        return respone(list);
    }

    @RequestMapping("/mappings")
    public Set mappings(){
        RequestMappingHandlerMapping mapping = applicationContext .getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        TreeSet set=new TreeSet();//所有可用的url
        for (RequestMappingInfo info : map.keySet()) {
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            set.addAll(patterns);
        }
        return set;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
