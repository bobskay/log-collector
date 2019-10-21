package wang.wangby.utils.threadpool.batch;

import lombok.Data;
import wang.wangby.annotation.Remark;
import wang.wangby.utils.DateTime;

import java.util.Date;

@Data
public class EachResult implements  InvokeBean{
    private Long begin;
    private Long end;
    private Long invokeBegin;
    private Long invokeEnd;
    @Remark("时间运行时间")
    private Date runTime;
    private Object result;

    public String getTotalTimes(){
        return DateTime.showNs(begin,end);
    }
    public String getRunTimes(){
        return DateTime.showNs(invokeBegin,invokeEnd);
    }
}
