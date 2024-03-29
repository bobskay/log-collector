package wang.wangby.utils.shell;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import wang.wangby.test.TestBase;

@Slf4j
public class LongTimeShellPoolTest extends TestBase {


    public static void main(String[] args) throws Exception {
        LongTimeShellPool pool= new LongTimeShellPool(new ShellClient(100,"test"),10,60);
        ShellExecInfo info=pool.addShell("tail -f /opt/logtask/logs/1.txt ","myos","root","root",true,true);
        Thread.sleep(2000L);
        log.debug("读到的数据"+pool.poll(info.getKey())+"");
        pool.kill(info.getKey());
    }

}