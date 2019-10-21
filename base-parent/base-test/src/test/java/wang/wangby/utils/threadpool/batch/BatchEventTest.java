package wang.wangby.utils.threadpool.batch;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;
import wang.wangby.test.assertutils.MyAssert;
import wang.wangby.utils.IdWorker;
import wang.wangby.utils.threadpool.ThreadPoolEvent;

import java.util.ArrayList;
import java.util.Random;

@Slf4j
public class BatchEventTest extends TestBase {

    @Test
    public void test() throws Exception {
        Random rd = new Random();
        BatchEvent event = BatchEvent.repeat(new ThreadPoolEvent() {
            @Override
            public Object call() throws Exception {
                int i = rd.nextInt(2);
                if (i == 0) {
                    throw new RuntimeException(i + "");
                }
                return new MyResult();
            }
        }, 10);

        BatchResult result = (BatchResult) event.call();
        BatchReport report = BatchReport.getReport(new ArrayList<>(result.getEachResults()),
                result.getCreate(),
                result.getRunBegin(), result.getRunEnd());
        MyAssert.notEmpty(report);
    }

    public static class MyResult implements InvokeBean{
        @Override
        public Long getInvokeBegin() {
            return 0L;
        }

        @Override
        public Long getInvokeEnd() {
            return 0L;
        }

        @Override
        public Object getResult() {
            return new Exception();
        }
    }

}
