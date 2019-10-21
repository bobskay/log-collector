package wang.wangby.logmanager.controller;

import com.jcraft.jsch.JSchException;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.api.Param;
import wang.wangby.annotation.api.Return;
import wang.wangby.annotation.web.Menu;
import wang.wangby.exception.Message;
import wang.wangby.logmanager.model.ServerInfo;
import wang.wangby.logmanager.service.ServerInfoService;
import wang.wangby.model.dao.Pagination;
import wang.wangby.model.request.Response;
import wang.wangby.utils.shell.Shell;
import wang.wangby.web.controller.BaseController;
import wang.wangby.web.tools.controller.vo.ShellInfo;

import java.io.IOException;

@RestController
@RequestMapping("serverInfo")
public class ServerInfoController extends BaseController {

    @Autowired
    ServerInfoService service;

    @RequestMapping("/index")
    @Remark("首页")
    @Menu(value = "服务器管理",icon = "server")
    public String index() {
        return $("index");
    }

    @RequestMapping("/prepareInsert")
    @Remark("进入新增页面")
    public String prepareInsert() {
        return $("prepareInsert");
    }


    @RequestMapping("/insert")
    @Remark("新增")
    @Param("要插入数据库的对象")
    @Return("新增成功后的数据,填充了主键")
    public Response<ServerInfo> insert(ServerInfo ServerInfo) {
        service.insert(ServerInfo);
        return respone(ServerInfo);
    }

    @RequestMapping("/select")
    @Remark("查询")
    @Param("查询条件")
    @Param("起始条数,从0开始")
    @Param("返回条数")
    @Return("分页后的查询结果")
    public Response<Pagination> select(ServerInfo ServerInfo, Integer offset, Integer limit) {
        Pagination page = service.selectPage(ServerInfo, offset, limit);
        return respone(page);
    }

    @Remark("通过主键删除")
    @RequestMapping("/deleteById")
    @Param("要删除的数据Id")
    @Return("删除记录数")
    public Response<Integer> deleteById(Long[] serverInfoId) {
        return respone(service.deleteById(serverInfoId));
    }

    @Remark("通过主键删除")
    @RequestMapping("/updateById")
    public Response<ServerInfo> updateById(ServerInfo serverInfo) {
        service.updateById(serverInfo);
        return respone(serverInfo);
    }


    @Remark("进入修改页面")
    @RequestMapping("/prepareUpdate")
    @Param("主键")
    public  String prepareUpdate(Long serverInfoId) {
        if (serverInfoId == null) {
            throw new Message("主键不能为空");
        }
        ServerInfo model = service.get(serverInfoId);
        return $("prepareUpdate",model);
    }

    @RequestMapping("shell")
    public Response<String> shell(ShellInfo shellInfo) throws JSchException, IOException {
        @Cleanup
        Shell shell=new Shell("myos","root","root");
        String s=shell.exec(shellInfo.getCommand());
        return respone(s);
    }
}
