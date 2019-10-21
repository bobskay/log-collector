package wang.wangby.utils.shell;

import com.jcraft.jsch.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import wang.wangby.utils.StrBuilder;
import wang.wangby.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

@Slf4j
public class Shell implements AutoCloseable{
	public static final int DEFAULT_PORT=22;
	public static final String BASE="/home/ming/bin";
	@Getter  private long lastTime;//最后执行时间
	private Session session;

	public Shell(String host,String username,String password) throws JSchException{
		this(host,DEFAULT_PORT,username,password);
	}

	public boolean kill(int port) throws JSchException,IOException{
		String command=BASE+"/kill";
		String result=this.exec(command);
		if(result.equals("success")){
			return true;
		}
		return false;
	}

	public Shell(String host,int port,String username,String password) throws JSchException{
		JSch jsch=new JSch();
		session=jsch.getSession(username,host,port);
		session.setPassword(password);
		java.util.Properties config=new java.util.Properties();
		config.put("StrictHostKeyChecking","no");
		session.setConfig(config);
		session.connect();
	}

	public void upload(String src,String dst) throws JSchException, SftpException {
		ChannelSftp channelSftp=(ChannelSftp) session.openChannel("sftp");
		channelSftp.connect();
		channelSftp.put(src, dst);
		channelSftp.quit();
	}

	//运行某个命令,
	public void run(String command,ShellOutput out) throws JSchException,IOException{
		lastTime=System.currentTimeMillis();
		ChannelExec channel=(ChannelExec)session.openChannel("exec");
		channel.setCommand(command+" 2>&1");
		channel.connect();
		InputStream in=channel.getInputStream();
		BufferedReader reader=new BufferedReader(new InputStreamReader(in));
		while(true){
			String line=reader.readLine();
			if(line==null){
				break;
			}
			out.out(line);
		}
	}

	public void runInChannel(String command, Consumer<ChannelExec> channeConsumer) throws JSchException,IOException{
		lastTime=System.currentTimeMillis();
		ChannelExec channel=(ChannelExec)session.openChannel("exec");
		channel.setCommand(command+" 2>&1");
		channel.connect();
		channeConsumer.accept(channel);
	}

	//执行shell
	public String exec(String command) throws JSchException,IOException{
		final StrBuilder sb=new StrBuilder();
		ShellOutput strOut=new ShellOutput(){
			public void out(String line){
				sb.appendLine(line);
			}
		};
		log.debug("执行shell:"+command);
		this.run(command,strOut);
		String str=sb.toString();
		if(log.isTraceEnabled()){
			log.trace(command+"返回内容:"+ StringUtil.subString(str,1000));
		}
		return str;
	}

	public void close(){
		session.disconnect();
	}
}