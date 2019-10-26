package wang.wangby.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.rolling.RollingFileAppender;
import org.slf4j.MDC;
import wang.wangby.utils.DateTime;
import wang.wangby.utils.LogUtil;
import wang.wangby.utils.StringUtil;

//输出到文件的格式
public class FileLayout extends LayoutBase<ILoggingEvent> {
    public static final String SPLIT="\t";

    @Override
    public String doLayout(ILoggingEvent event) {
        StringBuilder sb=new StringBuilder();
        sb.append(new DateTime(event.getTimeStamp()).toString(DateTime.Format.YEAR_TO_MILLISECOND)+SPLIT);
        sb.append(event.getLevel()+SPLIT);
        sb.append(event.getThreadName()+SPLIT);
        String requestId= MDC.get("requestId");
        if(StringUtil.isEmpty(requestId)){
            requestId="-";
        }
        sb.append(requestId+SPLIT);
        StackTraceElement[] trace=event.getCallerData();
        if(trace.length<1) {
            sb.append("-"+SPLIT+"-"+SPLIT+"-"+SPLIT);
        }
        int pos= trace[0].getClassName().lastIndexOf('.');
        String className=trace[0].getClassName().substring(pos+1);
        sb.append(className+SPLIT);
        sb.append(trace[0].getMethodName()+SPLIT);
        sb.append(trace[0].getLineNumber()+SPLIT);

        String message=event.getMessage().replace("\n","\\n").replace("\t","\\t");
        sb.append(message);

        //异常信息
        IThrowableProxy throwableProxy=event.getThrowableProxy();
        if(throwableProxy!=null){
            sb.append("\\n"+throwableProxy.getClassName()+":" +throwableProxy.getMessage()+"\\n");
            for(StackTraceElementProxy stack:throwableProxy.getStackTraceElementProxyArray()){
                sb.append("\\t");
                sb.append(stack.getStackTraceElement().getClassName()+" ");
                sb.append(stack.getStackTraceElement().getMethodName()+" ");
                sb.append(stack.getStackTraceElement().getLineNumber()+" ");
                sb.append("\\n");
            }
        }
        return sb.toString()+"\n";
    }
}
