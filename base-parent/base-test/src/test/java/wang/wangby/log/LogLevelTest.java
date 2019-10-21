package wang.wangby.log;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.utils.LogUtil;

@Slf4j
public class LogLevelTest extends TestBase {

    @Test
    public void changeLevel(){
        log.debug("可以看见debug");
        LogUtil.changeLevel(this.getClass(), Level.INFO);
        log.debug("leve改为jinfo了,debug消失");
        log.info("可以见info");
    }
}
