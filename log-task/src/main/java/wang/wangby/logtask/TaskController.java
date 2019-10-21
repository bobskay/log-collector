package wang.wangby.logtask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.web.controller.BaseController;

@RestController
@Slf4j
public class TaskController extends BaseController implements InitializingBean {

    @RequestMapping("/")
    public String index() {
        return "it works";
    }



}
