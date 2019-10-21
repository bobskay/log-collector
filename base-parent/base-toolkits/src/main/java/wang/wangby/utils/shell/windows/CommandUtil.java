package wang.wangby.utils.shell.windows;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.StringUtil;

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
		String str=getOutput(cmd);
		if(StringUtil.isEmpty(str)) {
			throw new RuntimeException("获取pid失败,请确认传入的应用名是否正确:"+cmd);
		}
		return OutputString.getInstance(str).getUnique(1);
	}

	
	public static String invoke(String ...cmds) throws Exception {
		String out="";
		for(String cmd:cmds) {
			cmd=replaceVar(cmd);
			if(cmd.startsWith("$")) {
				out=invokeMethod(cmd);
			}else {
				out=getOutput(cmd+" "+out);
			}
		}
		return out;
	}
	
	//方法名必须全小写,出参入都是字符串
	private static String invokeMethod(String cmd) throws Exception {
		try {
			int index=cmd.indexOf(" ");
			String methodName=cmd.substring(1, index);
			Method m=CommandUtil.class.getMethod(methodName.toLowerCase(), String.class);
			return (String) m.invoke(CommandUtil.class, cmd.substring(index+1));
		}catch(Exception ex) {
			throw new RuntimeException("执行命令出错:"+cmd,ex);
		}
	}
	
	

	//替换环境变量
	private static String replaceVar(String cmd) {
		String regx = "(?s)(?i)" + "\\$\\{.*\\}";
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(cmd);
		while (matcher.find()) {
			String var=matcher.group();
			String value=getVar(var);
			cmd=cmd.replace(var, value);
		}
		return cmd;
	}

	public static String getVar(String param) {
		String name=param.substring(2,param.length()-1);
		String str=getOutput("set "+name);
		if(StringUtil.isEmpty(str.trim())) {
			return param;
		}
		return OutputString.getInstance(str, "=").getUnique(1);
	}
	
	
	public static String getOutput(String cmd) {
		try {
			cmd="cmd /c "+cmd;
			log.debug(cmd);
			Runtime run = Runtime.getRuntime();
			Process process = run.exec(cmd);
			InputStream in = process.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(reader);
			StringBuffer sb = new StringBuffer();
			String message;
			while ((message = br.readLine()) != null) {
				sb.append(message);
				sb.append(StringUtil.line());
			}
			return sb.toString().trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
