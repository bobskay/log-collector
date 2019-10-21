package wang.wangby.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
public class HttpUtilTest extends TestBase {

    @Test
    public void getTest() throws IOException {
        String s = HttpUtil.get(Config.TESTURL);
        log.debug("httpGet结果\n" + s);
    }
}
