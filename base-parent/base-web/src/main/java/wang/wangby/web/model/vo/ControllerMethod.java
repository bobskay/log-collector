package wang.wangby.web.model.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import wang.wangby.annotation.Remark;
import wang.wangby.annotation.web.Menu;

import java.lang.reflect.Method;

//controller的method信息
@Data
public class ControllerMethod {
    @Remark("原始的method类")
    private transient Method method;
    private Menu menu;
    private Remark remark;
    private RequestMapping requestMapping;
    @Remark("类上的requestMapping信息")
    private RequestMapping classRequestMapping;
    @Remark("类上的备注信息")
    private Remark classRemark;


}
