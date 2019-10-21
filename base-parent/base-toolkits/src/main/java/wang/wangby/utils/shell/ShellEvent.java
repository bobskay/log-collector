package wang.wangby.utils.shell;

import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.wangby.utils.threadpool.Event;

public class ShellEvent extends Event {
	private static Logger log = LoggerFactory.getLogger(ShellEvent.class);

	private Shell shell;
	private String command;
	private ShellOutput out;
	
	public ShellEvent(Shell shell,String command,ShellOutput out) throws JSchException {
		this.shell=shell;
		this.out=out;
		this.command=command;
	}


	@Override
	public void execute() throws Exception {
		try{
			shell.run(command, out);
		}catch(Exception ex){
			log.info(ex.getMessage(),ex);
		}
	}
	
}
