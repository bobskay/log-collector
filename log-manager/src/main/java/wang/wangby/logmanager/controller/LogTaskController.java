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
import wang.wangby.logmanager.config.LogManagerProperties;
import wang.wangby.logmanager.model.LogFile;
import wang.wangby.logmanager.model.LogTask;
import wang.wangby.logmanager.model.ServerInfo;
import wang.wangby.logmanager.model.vo.TaskRunningInfo;
import wang.wangby.logmanager.service.LogFileService;
import wang.wangby.logmanager.service.LogTaskService;
import wang.wangby.logmanager.service.ServerInfoService;
import wang.wangby.model.dao.Pagination;
import wang.wangby.model.request.Response;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.web.controller.BaseController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("logTask")
public class LogTaskController extends BaseController {

    @Autowired
    LogTaskService logTaskService;
    @Autowired
    ServerInfoService serverInfoService;
    @Autowired
    LogFileService logFileService;
    @Autowired
    ShellClient shellClient;
    @Autowired
    LogManagerProperties logManagerProperties;


    @RequestMapping("/index")
    @Menu("日志收集任务")
    public String index(LogTask logTask) {
        Map map=new HashMap();
        map.put("logTask",logTask);
        return $("index");
    }

    @RequestMapping("/prepareInsert")
    @Remark("进入新增页面")
    public String prepareInsert(Long logFileId) {
        ServerInfo serverInfo = new ServerInfo();
        List<ServerInfo> list = serverInfoService.select(serverInfo, 0, 100);
        List<LogFile> files = logFileService.select(new LogFile(), 0, 100);
        for (LogFile file : files) {
            for (ServerInfo s : list) {
                if (file.getServerInfoId().equals(s.getServerInfoId())) {
                    file.setServerInfo(s);
                    break;
                }
            }
        }
        Map map = new HashMap<>();
        map.put("serverInfoList", list);
        map.put("logFileList", files);
        map.put("logFileId", logFileId);
        return $("prepareInsert", map);
    }


    @RequestMapping("/insert")
    @Remark("新增数据")
    @Param("要插入数据库的对象")
    @Return("新增成功后的数据,填充了主键")
    public Response<LogTask> insert(@RequestBody  LogTask logTask) {
        logTaskService.insert(logTask);
        return respone(logTask);
    }

    @RequestMapping("/select")
    @Remark("查询图书")
    @Param("查询条件")
    @Param("起始条数,从0开始")
    @Param("返回条数")
    @Return("分页后的查询结果")
    public Response<Pagination> select(LogTask logTask, Integer offset, Integer limit) {
        Pagination page = logTaskService.selectPage(logTask, offset, limit);
        return respone(page);
    }

    @Remark("通过主键删除")
    @RequestMapping("/deleteById")
    @Param("要删除的数据Id")
    @Return("删除记录数")
    public Response<Integer> deleteById(Long[] logTaskId) {
        return respone(logTaskService.deleteById(logTaskId));
    }

    @Remark("通过主键删除图书")
    @RequestMapping("/updateById")
    public Response<LogTask> updateById(LogTask logTask) {
        logTaskService.updateById(logTask);
        return respone(logTask);
    }


    @Remark("进入修改页面")
    @RequestMapping("/prepareUpdate")
    @Param("主键")
    public String prepareUpdate(Long logTaskId) {
        if (logTaskId == null) {
            throw new Message("主键不能为空");
        }
        LogTask model = logTaskService.get(logTaskId);
        return $("prepareUpdate", model);
    }

    //验证配置信息是否正确
    @RequestMapping("doTest")
    public Response<TaskRunningInfo> doTest(@RequestBody LogTask logTask) throws IOException, JSchException {
        TaskRunningInfo info = logTaskService.runningInfo(logTask);
        return respone(info);
    }

    @Remark("启动计划任务")
    @RequestMapping("start")
    public Response<String> start(Long logTaskId){
        try{
            LogTask logTask=logTaskService.run(logTaskId,logManagerProperties.getTaskJar());
            return respone(logTask);
        }catch (Exception ex){
            log.warn(ex.getMessage(),ex);
            return Response.fail("启动任务失败:"+ex.getMessage());
        }
    }
}
