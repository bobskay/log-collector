package wang.wangby.utils.shell;

import com.jcraft.jsch.JSchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.wangby.utils.threadpool.Event;
import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**执行shell的线程池*/
public class ShellPool{
	private static Logger log= LoggerFactory.getLogger(ShellPool.class);
	private ThreadPool pool;
	private Map<Long,LongTimeShell> map;
	private String defaultHost;
	private String username;
	private String password;

	/**
	 * 创建一个执行shell的线程池
	 * 
	 * @param size 线程池大小
	 * @param maxActive 当shell多久没更新就自动关闭
	 */
	public ShellPool(int size,final long maxActive,String host,String username,String password){
		this.defaultHost=host;
		this.username=username;
		this.password=password;
		pool= ThreadPoolFactory.newPool(this.getClass(),size);
		map=new ConcurrentHashMap();
		new Thread(){
			public void run(){
				while(true){
					try{
						Thread.sleep(1000L);
					}catch(Exception ex){
						log.error(ex.getMessage(),ex);
					}

					List<Long> removeId=new ArrayList();
					for(LongTimeShell shell:map.values()){
						try{
							if(System.currentTimeMillis()-shell.getUpdateTime().getTime()>maxActive){
								shell.close();
								removeId.add(shell.getId());
								log.debug("关闭:"+shell.getId());
							}
						}catch(Exception ex){
							log.error("关闭shell出错"+ex.getMessage(),ex);
						}
					}

					for(Long l:removeId){
						map.remove(l);
					}
				}
			}
		}.start();
	}

	
	public void addShell(Long id,final String command) throws JSchException{
		this.addShell(id, defaultHost, username, password, command);
	}
	
	/**
	 * 新增一个shell
	 * @param id 唯一标识,后续需要通过这个标识获取shell执行结果
	 * @param host 执行shell的主机
	 * @param username 用户名
	 * @param password 密码
	 * @param command 执行命令
	 * */
	public void addShell(Long id,String host,String username,String password,final String command) throws JSchException{
		final Shell shell=new Shell(host,username,password);
		final LongTimeShell out=new LongTimeShell(id,shell);
		pool.execute(new Event(){
			@Override
			public void execute() throws Exception{
				shell.run(command,out);
			}
		});
		map.put(id,out);
	}

	public String pool(Long id){
		LongTimeShell shell=map.get(id);
		if(shell==null){
			return "已关闭";
		}
		shell.setUpdateTime(new Date());
		return map.get(id).poll();
	}

	public void close(Long id){
		LongTimeShell shell=map.get(id);
		if(shell==null){
			return;
		}
		shell.close();
		map.remove(id);
	}
}
