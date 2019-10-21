package wang.wangby.logtask.processor;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.logtask.model.LogParseResult;

@Slf4j
public class LogProcessor {

    public boolean doProcess(LogParseResult logParseResult){
        log.info("收到解析后的数据:position={},data={},error={}",
                logParseResult.getEnd(),
                logParseResult.getData().size(),
                logParseResult.getErrors().size());
        return true;
    }
}
