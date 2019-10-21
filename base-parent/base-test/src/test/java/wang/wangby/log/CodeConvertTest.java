package wang.wangby.log;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;
import wang.wangby.test.TestBase;
import wang.wangby.utils.IdWorker;

@Slf4j
public class CodeConvertTest extends TestBase {


	@Test
	public void testConvertILoggingEvent() {
		String uuid=IdWorker.nextString();
		MDC.put("requestId", uuid);
		int machineNo=IdWorker.getMachineNo(uuid);

		log.debug("hello debug "+machineNo );
		log.info("hello info");
		log.warn("hello warn");
	}

	
	@Test
	public void testCharset() {
		log.debug("中文测试");
	}


	@Test
	public void testLevel() {
		log.debug("debug");
		log.info("info");
		log.warn("warn");
		log.error("error");
	}
}
