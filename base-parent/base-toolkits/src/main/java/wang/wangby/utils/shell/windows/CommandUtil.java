package wang.wangby.utils.shell.windows;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.StrBuilder;
import wang.wangby.utils.StringUtil;
import wang.wangby.utils.shell.ShellOutput;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommandUtil {

	public static String pid(String appName) {
		String cmd = "tasklist|findstr "+ appName;
		StrBuilder sb=new StrBuilder();
		ShellOutput output=new ShellOutput() {
			@Override
			public void out(String line) {
				sb.appendLine(line);
			}
		};
	     run(cmd,output);
		if(StringUtil.isEmpty(sb.toString())) {
			throw new RuntimeException("获取pid失败,请确认传入的应用名是否正确:"+cmd);
		}
		return sb.toString();
	}


	public static void run(String cmd,ShellOutput output) {
		try {
			cmd="cmd /c "+cmd;
			log.debug("准备执行命令:"+cmd);
			Runtime run = Runtime.getRuntime();
			Process process = run.exec(cmd);
			InputStream in = process.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);
			StringBuffer sb = new StringBuffer();
			String message=null;
			while ((message = br.readLine()) != null) {
				output.out(message);
			}
		} catch (Exception e) {
			log.error("执行"+cmd+"出错",e);
		}
	}
	

}
