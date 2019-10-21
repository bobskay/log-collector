package wang.wangby.web.tools.controller.vo;

import lombok.Data;

@Data
public class ShellInfo {
    private String hostname;
    private String command;
    private String username;
    private String password;
}
