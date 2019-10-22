package wang.wangby.web.tools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.model.request.Response;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.shell.LongTimeShellPool;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.utils.shell.ShellExecInfo;
import wang.wangby.web.controller.BaseController;
import wang.wangby.web.tools.controller.vo.ShellInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@RequestMapping("shell")
@RestController
@Menu("工具箱")
public class ShellController extends BaseController {
    private Map<String, BlockingQueue> map;

    @Autowired
    LongTimeShellPool longTimeShellPool;
    @Autowired
    ShellClient shellClient;

    @Menu("执行shell")
    @RequestMapping("index")
    public String index() {
        return $("index");
    }

    @RequestMapping("exec")
    public Response<Map> exec(@RequestBody  ShellInfo shellInfo) throws Exception {
        if(StringUtil.isNotEmpty(shellInfo.getKey())){
            longTimeShellPool.kill(shellInfo.getKey());
        }
        ShellExecInfo info= longTimeShellPool.addShell(shellInfo.getCommand(),shellInfo.getHostname(),shellInfo.getUsername(),shellInfo.getPassword());
        Map<String,Object> map=new HashMap<>();
        if(StringUtil.isNotEmpty(info.getKey())){
            map.put("key",info.getKey());
        }else{
            map.put("datas",info.getDatas());
        }
       return respone(map);
    }

    @RequestMapping("read")
    public Response<List<String>> read(String key){
        List<String> data=longTimeShellPool.poll(key);
        if(data==null){
            return Response.fail("shell已经关闭");
        }
        return respone(data);
    }

}
