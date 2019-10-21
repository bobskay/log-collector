package wang.wangby.logtask.parser;

import wang.wangby.logtask.model.ErrorInfo;
import wang.wangby.logtask.model.JavaLog;
import wang.wangby.logtask.model.LogParseResult;
import wang.wangby.utils.DateTime;
import wang.wangby.utils.LogUtil;
import wang.wangby.utils.StrBuilder;
import wang.wangby.utils.StringPicker;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogbackParser implements LogParser<JavaLog> {

    private String beginPattern;
    private String dateFormat;
    private String charset;

    public LogbackParser(String beginPattern, String dateFormat,String charset) {
        this.beginPattern = beginPattern;
        this.dateFormat = dateFormat;
        this.charset=charset;

    }
    /**
     * @param  begin 起始位置,从1开始
     * @param  text 读取到的文件内容
     * @param  isEnd 是否读到了文件末尾,如果是末尾就要处理最后一条记录
     * */
    public LogParseResult<JavaLog> parse(Long begin,String text,boolean isEnd) throws Exception {
        List<JavaLog> logs = new ArrayList<>();
        List<ErrorInfo> errors = new ArrayList<>();
        StringPicker picker = new StringPicker(text);
        StrBuilder last = new StrBuilder();
        while (true) {
            String line = picker.next("\n");
            if (line == null) {
                break;
            }
            if (line.matches(beginPattern)) {
                begin=begin+addLog(begin,logs, errors, last.toString());
                last = new StrBuilder();
            }
            last.append(line + "\n");
        }
        LogParseResult result = new LogParseResult();
        String remain=last+picker.nextAll();
        if(isEnd){
            begin=begin+addLog(begin,logs,errors,remain);
            remain="";
        }
        result.setReamin(remain);
        result.setData(logs);
        result.setErrors(errors);
        result.setEnd(begin-1);
        return result;

    }

    private int addLog(Long begin,List<JavaLog> logs, List<ErrorInfo> errors, String str) throws UnsupportedEncodingException {
        if (str.length() == 0) {
            return 0;
        }
        int length=str.getBytes(charset).length;
        try {
            StringPicker picker = new StringPicker(str);
            String logTimeStr = picker.next(dateFormat.length());
            JavaLog log = new JavaLog();
            Date logTime = DateTime.getFormat(dateFormat).parse(logTimeStr);
            picker.next(" ");
            log.setLevel(picker.next(5).trim());
            picker.next(" ");
            log.setLogTime(logTime);
            log.setThreadName(picker.next(" "));
            log.setRequestId(picker.next(" "));
            log.setClassName(picker.next("."));
            log.setMethodName(picker.next(":"));
            String line = picker.next(" ");
            line=line.replace(")", "");//如果是warn以上的打印是会将方法括起来
            log.setLine(Integer.parseInt(line));
            log.setMessage(picker.nextAll());
            log.setBegin(begin);
            log.setLength(length);
            logs.add(log);
        } catch (Exception ex) {
            ErrorInfo errorLog = new ErrorInfo();
            errorLog.setContent(str);
            errorLog.setErrorInfo(LogUtil.getExceptionText(ex));
            errorLog.setBegin(begin);
            errorLog.setLength(length);
            errors.add(errorLog);
        }
        return length;
    }


}
