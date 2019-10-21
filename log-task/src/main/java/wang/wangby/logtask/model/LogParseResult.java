package wang.wangby.logtask.model;

import lombok.Data;

import java.util.List;

@Data
public class LogParseResult<T> {

    private List<T> data;
    private List<ErrorInfo> errors;
    private String reamin;
    private Long end;
}
