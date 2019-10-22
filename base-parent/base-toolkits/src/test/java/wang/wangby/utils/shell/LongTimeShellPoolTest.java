package wang.wangby.utils.shell;

import org.junit.Test;
import wang.wangby.test.TestBase;

public class LongTimeShellPoolTest extends TestBase {

    @Test
    public void addShell() throws Exception {
        LongTimeShellPool pool= new LongTimeShellPool(new ShellClient(100,"test"),10,60);
        ShellExecInfo info=pool.addShell("tail -f /opt/logtask/logs/1.txt ","myos","root","root",true,true);
        Thread.sleep(2000L);
        log.debug("读到的数据"+pool.poll(info.getKey())+"");
        pool.kill(info.getKey());
    }
}