package wang.wangby.web.tools.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.utils.threadpool.ScheduledFactory;
import wang.wangby.utils.threadpool.job.JobInfo;
import wang.wangby.web.controller.BaseController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("scheduledManager")
@Menu("工具箱")
public class ScheduledManagerController  extends BaseController {

    @RequestMapping("/index")
    @Menu("计划任务管理")
    public String index( ) {
        Collection collection= ScheduledFactory.secheduleMap().values();
        List<JobInfo> jobs=new ArrayList<>(collection);
        return $("index",jobs);
    }


    @RequestMapping("/pause")
    public Response<String> pause(String name) {
        ScheduledFactory.pause(name);
        return respone("ok");
    }

    @RequestMapping("/resume")
    public Response<String> resume(String name) {
        ScheduledFactory.resume(name);
        return respone("ok");
    }
}
