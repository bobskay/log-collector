package wang.wangby.logtask.model;

import lombok.Data;

@Data
public class ErrorInfo {
    private Long begin;
    private Integer length;
    private String content;
    private String errorInfo;
}
