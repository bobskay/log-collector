package wang.wangby.codebuilder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.codebuilder.controller.vo.CodeConfig;
import wang.wangby.codebuilder.controller.vo.RunningInfo;
import wang.wangby.codebuilder.service.CodeBuilderService;
import wang.wangby.model.request.Response;
import wang.wangby.utils.IdWorker;
import wang.wangby.utils.shell.windows.CommandUtil;
import wang.wangby.utils.threadpool.Event;
import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolFactory;
import wang.wangby.web.controller.BaseController;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("codeBuilder")
@RestController
public class CodeBuilderController extends BaseController {

    private Map<String, RunningInfo> outputMap=new ConcurrentHashMap<>();
    private ThreadPool pool= ThreadPoolFactory.newPool("CodeBuilderController",10);

    @Autowired
    CodeBuilderService codeBuilderService;

    @RequestMapping("index")
    @Menu("首页")
    public String index(){
        return $("index");
    }

    @RequestMapping("createAllInOne")
    public Response<String> createAllInOne(@RequestBody CodeConfig codeConfig){
        return respone("ok");
    }

    @RequestMapping("createCode")
    public Response<String> createCode(@RequestBody CodeConfig codeConfig){
        String template=this.getClass().getResource("/codeTemplate/"+codeConfig.getProjectType()).getPath();
        String output=codeConfig.getOutputDir()+"/"+codeConfig.getAppName();
        codeBuilderService.createCode(codeConfig,template,output);
        return respone("创建成功,文件路径:"+new File(output).getAbsolutePath());
    }

    @RequestMapping("getRunningInfo")
    public Response<List<String>> getRunningInfo(String id){
        List datas=new ArrayList<>();
        RunningInfo inf=outputMap.get(id);
        if(inf==null){
            datas.add("程序已经执行完毕");
        }else{
            while (true){
                String data=inf.getDatas().poll();
                if(data==null){
                    break;
                }
                datas.add(data);
            }
        }
        return respone(datas);
    }

    @RequestMapping("runApp")
    public Response<String> runApp(@RequestBody CodeConfig codeConfig){
        String template=this.getClass().getResource("/codeTemplate/"+codeConfig.getProjectType()).getPath();
        String output=codeConfig.getOutputDir()+"/"+codeConfig.getAppName();
        codeBuilderService.createCode(codeConfig,template,output);

        String id= IdWorker.nextString();
        RunningInfo out=new RunningInfo();
        out.setAppName(codeConfig.getAppName());
        out.setDatas(new ArrayBlockingQueue<>(1024));
        outputMap.put(id,out);
        String command="cd "+output+" && mvn install && java -jar ./target/"+codeConfig.getAppName()+"-0.1.jar ";

        pool.execute(new Event() {
            @Override
            public void execute() throws Exception {
                CommandUtil.run(command,str->{
                    out.getDatas().offer(str);
                });
            }
        });
        return respone(id);
    }

    @RequestMapping("showAllRunning")
    public String showAllRunning(){
        Map map=new HashMap();
        map.put("runningInfos",outputMap.values());
       return $("showAllRunning",map);
    }
}
