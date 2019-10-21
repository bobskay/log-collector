package wang.wangby.web.webfilter.stack;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.monitor.MonitorUtil;
import wang.wangby.utils.monitor.vo.InvokeThread;
import wang.wangby.web.webfilter.WebFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
//打印所有堆栈信息的过滤器
//在调用方法前使用: MonitorUtil.begin("className.methodName");
//方法结束后： MonitorUtil.end("className.methodName");
//可用aspect或者javaAgent实现
public class  InvokeStackFilter implements WebFilter<String> {

    int stackIgnoreNs;
    public InvokeStackFilter(int stackIgnoreNs){
        this.stackIgnoreNs=stackIgnoreNs;
    }

    @Override
    public String begin(HttpServletRequest request, HttpServletResponse httpResponse) throws Exception {
        if(!log.isDebugEnabled()){
            return "";
        }
        MonitorUtil.init("InvokeStackFilter.begin");
        return "";
    }

    @Override
    public void end(String s, HttpServletRequest request) throws Exception {
        if(!log.isDebugEnabled()){
            return;
        }
        InvokeThread thread=MonitorUtil.finish();
        StringBuilder sb=new StringBuilder();
        thread.interator(m->{
            String pix= StringUtil.createString(m.getLevel(),"--");
            //忽略0.1ms一下的方法
            double time=(m.getEnd()-m.getBegin());
            if(time<stackIgnoreNs){
                return;
            }
            time=time/1000/1000;
            sb.append(pix+m.getName()+","+time);
            sb.append("\n");
        });
        log.debug("\n"+sb.toString().trim());
    }
}
