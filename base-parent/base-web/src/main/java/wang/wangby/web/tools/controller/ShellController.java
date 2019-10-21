package wang.wangby.web.tools.controller;

import com.jcraft.jsch.JSchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wang.wangby.annotation.web.Menu;
import wang.wangby.utils.shell.Shell;
import wang.wangby.utils.shell.ShellClient;
import wang.wangby.web.controller.BaseController;
import wang.wangby.web.tools.controller.vo.ShellInfo;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

@RequestMapping("shell")
@RestController
@Menu("工具箱")
public class ShellController extends BaseController {
    private Map<String, BlockingQueue> map;

    @Autowired
    ShellClient shellClient;

    @Menu("执行shell")
    public String index() {
        return $("index");
    }

    public void out(ShellInfo shellInfo) throws IOException, JSchException {
        Shell shell=shellClient.getShell(shellInfo.getHostname(),shellInfo.getUsername(),shellInfo.getPassword());
        shell.run(shellInfo.getCommand(),str->{
        });
    }

}
