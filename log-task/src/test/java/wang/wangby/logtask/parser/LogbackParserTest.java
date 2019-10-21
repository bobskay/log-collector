package wang.wangby.logtask.parser;

import org.junit.Test;
import wang.wangby.logtask.model.ErrorInfo;
import wang.wangby.logtask.model.JavaLog;
import wang.wangby.logtask.model.LogParseResult;
import wang.wangby.test.TestBase;
import wang.wangby.utils.FileUtil;

public class LogbackParserTest extends TestBase {

    //2019-10-20 20:44:34.252
    @Test
    public void parse() throws Exception {
        String dataformat = "yyyy-MM-dd HH:mm:ss.SSS";
        String beginPattern = "[\\d|\\-|:|.| ]{23}(.|\\s)*";
        String text = FileUtil.getText(this.getClass(),"/test.log");
        log.debug("文件大小:"+text.getBytes("UTF-8").length);

        LogbackParser parser = new LogbackParser(beginPattern, dataformat,"UTF-8");
        LogParseResult<JavaLog> result = parser.parse(1L,text,false);
        log.debug("---------------解析完毕:"+result.getData().size()+","+result.getErrors().size()+"-----");
        log.debug("---------------剩余未处理--------------\n"+result.getReamin());
        for(JavaLog javaLog:result.getData()){
            log.debug(javaLog.getBegin()+"+"+javaLog.getLength()+"="+(javaLog.getBegin()+javaLog.getLength()));
        }
        log.debug("-------错误内容-----------------------\n");
        for(ErrorInfo errorInfo:result.getErrors()){
            log.debug(errorInfo.getErrorInfo()+":"+errorInfo.getContent());
        }
        log.debug("结束位置:"+result.getEnd());

        byte[] bs = text.getBytes("UTF-8");
        int begin=result.getEnd().intValue();
        int end=bs.length-begin;
        String str=new String(bs,begin,end,"UTF-8");
        log.debug("第二次读取到的内容\n"+str);
    }
}