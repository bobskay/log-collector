package wang.wangby.logmanager.controller;

import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.api.Param;
import wang.wangby.annotation.api.Return;
import wang.wangby.annotation.web.Menu;
import wang.wangby.exception.Message;
import wang.wangby.logmanager.controller.vo.FileSearchResult;
import wang.wangby.logmanager.model.LogFile;
import wang.wangby.logmanager.model.ServerInfo;
import wang.wangby.logmanager.service.LogFileService;
import wang.wangby.logmanager.service.ServerInfoService;
import wang.wangby.model.dao.Pagination;
import wang.wangby.model.request.Response;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.web.controller.BaseController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Menu("日志文件管理")
@RequestMapping("logFile")
public class LogFileController extends BaseController {


    @Autowired
    LogFileService service;
    @Autowired
    ServerInfoService serverInfoService;

    @RequestMapping("/list")
    @Menu(value = "文件列表",icon = "file")
    public String list(LogFile logFile) {
        ServerInfo query=new ServerInfo();
        List<ServerInfo> list=serverInfoService.select(query,0,100);
        Map map=new HashMap<>();
        map.put("serverInfoList",list);
        map.put("logFile",logFile);
        return $("list",map);
    }


    @RequestMapping("/prepareInsert")
    @Menu("新增文件")
    public String prepareInsert() {
        ServerInfo query=new ServerInfo();
        List<ServerInfo> list=serverInfoService.select(query,0,100);
        return $("prepareInsert",list);
    }


    @RequestMapping("/insert")
    @Remark("新增")
    @Param("要插入数据库的对象")
    @Return("新增成功后的数据,填充了主键")
    public Response<LogFile> insert(@RequestBody  LogFile logFile) {
        service.insert(logFile);
        return respone(logFile);
    }

    @RequestMapping("/select")
    @Remark("查询")
    @Param("查询条件")
    @Param("起始条数,从0开始")
    @Param("返回条数")
    @Return("分页后的查询结果")
    public Response<Pagination> select(LogFile logFile, Integer offset, Integer limit) {
        Pagination page = service.selectPage(logFile, offset, limit);
        return respone(page);
    }

    @Remark("通过主键删除")
    @RequestMapping("/deleteById")
    @Param("要删除的数据Id")
    @Return("删除记录数")
    public Response<Integer> deleteById(Long[] logFileId) {
        return respone(service.deleteById(logFileId));
    }

    @Remark("通过主键删除")
    @RequestMapping("/updateById")
    public Response<LogFile> updateById(LogFile logFile) {
        service.updateById(logFile);
        return respone(logFile);
    }


    @Remark("进入修改页面")
    @RequestMapping("/prepareUpdate")
    @Param("主键")
    public  String prepareUpdate(Long LogFileId) {
        if (LogFileId == null) {
            throw new Message("主键不能为空");
        }
        LogFile model = service.get(LogFileId);
        return $("prepareUpdate",model);
    }

    @RequestMapping("searchFile")
    public Response<String> searchFile(@RequestBody LogFile logFile) throws JSchException, IOException {
        ServerInfo serverInfo=serverInfoService.get(logFile.getServerInfoId());
        if(serverInfo==null){
            return respone("找不到服务器"+logFile.getServerInfoId());
        }

        String command="ls "+logFile.getPath();
        String result= ShellClient.getDefault().exec(command,serverInfo.getHostName(),logFile.getUsername(),logFile.getPasswd());
        List<FileSearchResult> resultList=new ArrayList<>();
        for(String fileName:result.split(StringUtil.REG_BLANK)){
            FileSearchResult sr=new FileSearchResult();
            sr.setDir(logFile.getPath());
            sr.setName(fileName);
            if(StringUtil.isEmpty(logFile.getFileNameRegx())){
                sr.setMatch(true);
            }else{
                sr.setMatch(sr.getName().matches(logFile.getFileNameRegx()));
            }

            resultList.add(sr);
        }
        return respone(resultList);
    }
}
