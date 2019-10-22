package wang.wangby.web.tools.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.web.controller.BaseController;

@RestController
@RequestMapping("timeoutTest")
@Menu("工具箱")
public class TimeoutTestController extends BaseController {

    @RequestMapping("/sleep")
    public Response<String> sleep(int millis) throws InterruptedException {
        Thread.sleep(millis);
        return Response.success("ok");
    }

    public Response<String> sleepFallback(int millis) throws InterruptedException {
        return Response.success("sleep超时");
    }

}
