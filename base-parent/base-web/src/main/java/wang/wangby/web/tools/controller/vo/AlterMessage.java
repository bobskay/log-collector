package wang.wangby.web.tools.controller.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AlterMessage {

    private Date receiveTime;
    private Object message;
}
