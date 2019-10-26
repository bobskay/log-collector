package wang.wangby.utils.shell.windows;

import org.junit.Test;
import wang.wangby.utils.shell.ShellOutput;

import static org.junit.Assert.*;

public class CommandUtilTest {

    @Test
    public void invoke() throws Exception {
        ShellOutput output=new ShellOutput() {
            @Override
            public void out(String line) {
                System.out.println(line);
            }
        };
       CommandUtil.run("cd E:\\2019\\log-collector\\examples\\helloWorld && mvn install && java -jar ./target/helloWorld-0.1.jar ",output);
    }
}