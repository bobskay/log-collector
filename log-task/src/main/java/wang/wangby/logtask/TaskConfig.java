package wang.wangby.logtask;

import lombok.Data;

@Data
public class TaskConfig {
    private Long taskId;
    private String serverName;
    private String charset;
}
