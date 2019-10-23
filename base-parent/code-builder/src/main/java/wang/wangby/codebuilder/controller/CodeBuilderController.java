package wang.wangby.codebuilder.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.codebuilder.controller.vo.CodeConfig;
import wang.wangby.model.request.Response;
import wang.wangby.web.controller.BaseController;

@RequestMapping("codeBuilder")
@RestController
public class CodeBuilderController extends BaseController {

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

        return respone("ok");
    }
}
