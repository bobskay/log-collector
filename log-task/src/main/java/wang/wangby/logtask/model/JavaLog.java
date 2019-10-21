package wang.wangby.logtask.model;

import lombok.Data;

import java.util.Date;

@Data
public class JavaLog {
    private Date logTime;
    private String level;
    private String className;
    private String methodName;
    private Integer line;
    private String message;
    private Date createTime;
    private String threadName;
    private String requestId;
    private Long begin;
    private Integer length;
}
