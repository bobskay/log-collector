package wang.wangby.log;

import ch.qos.logback.access.PatternLayout;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;
import sun.reflect.misc.FieldUtil;
import wang.wangby.utils.DateTime;
import wang.wangby.utils.FileUtil;
import wang.wangby.utils.IdWorker;

import java.io.File;

@Slf4j
public class FileLayoutTest {

    @Test
    public void test() {
        String date = DateTime.current().toString(DateTime.Format.YEAR_TO_DAY_STRING);
        String fileName = "/opt/logs/test" + date + ".log";
        log.debug("测试格式化后的消息:\n回车是否替换\n");
        log.debug("需要查看文件确认:" + new File(fileName).getAbsolutePath());
        log.error("查看异常情况", new Exception("test"));

    }
}
