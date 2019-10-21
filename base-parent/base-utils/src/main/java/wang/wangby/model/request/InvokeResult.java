package wang.wangby.model.request;

import lombok.Data;
import wang.wangby.annotation.Remark;

@Data
@Remark("调用结果")
public class InvokeResult {
    @Remark("生成时间(ms)")
    private long createTime;
    @Remark("耗时")
    private long times;
    @Remark("名称,唯一标识某个请求,url或者方法名,或者其它唯一标示")
    private String name;
    @Remark("参数")
    private String param;
    @Remark("返回值")
    private String result;

    //{'className':{'name':'xx','times':1234}}
    public static  String toJs(String name,Long times,String param){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        sb.append("\"").append(InvokeResult.class.getName()).append("\":");
        sb.append("{");
        sb.append("name:");
        sb.append("\"").append(name).append("\",");
        sb.append("times:");
        sb.append(times);
        sb.append(",param:");
        sb.append("\"").append(param).append("\"");
        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    //{createTime:12345678,name:"xx",times:1234,param="xxx",result="200"}
    //param如果包含"需要在外面自己去掉
    public static  String toInvoke(String name,long times,String param,String result){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        sb.append("createTime:"+System.currentTimeMillis()).append(",");
        sb.append("name:");
        sb.append("\"").append(name).append("\",");
        sb.append("times:");
        sb.append(times);
        sb.append(",param:");
        sb.append("\"").append(param).append("\"");
        sb.append(",result:");
        sb.append("\"").append(result).append("\"");
        sb.append("}");
        return sb.toString();
    }
}
