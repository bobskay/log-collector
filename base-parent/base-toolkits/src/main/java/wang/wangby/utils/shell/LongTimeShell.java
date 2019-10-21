package wang.wangby.utils.shell;


import wang.wangby.utils.StrBuilder;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

//耗时比较久的shell
public class LongTimeShell implements ShellOutput{

	private Date updateTime;//最后更新时间,如果此shell长时间没更新,就关闭
	private Shell shell;
	private Queue<String> queue=new ArrayBlockingQueue<>(1024);
	private Long id;

	public LongTimeShell(Long id,Shell shell){
		this.shell=shell;
		this.id=id;
		this.updateTime=new Date();
	}

	@Override
	public void out(String line){
		queue.add(line);
	}

	public String poll(){
		StrBuilder sb=new StrBuilder();
		while(queue.size()>0){
			sb.appendLine(queue.poll());
		}
		return sb.toString().trim();
	}

	public void close(){
		shell.close();
	}

	public Date getUpdateTime(){
		return updateTime;
	}

	public void setUpdateTime(Date updateTime){
		this.updateTime=updateTime;
	}

	public Long getId(){
		return id;
	}

}
