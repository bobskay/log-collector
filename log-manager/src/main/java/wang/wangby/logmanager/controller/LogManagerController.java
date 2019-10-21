package wang.wangby.logmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.web.controller.BaseController;

@RestController
@RequestMapping("logManager")
public class LogManagerController extends BaseController {

    @Menu(value = "首页",icon = "dashboard")
    @RequestMapping("/index")
    public String index(){
        return $("index");
    }
}
