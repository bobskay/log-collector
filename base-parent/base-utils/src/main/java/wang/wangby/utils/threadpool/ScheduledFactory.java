package wang.wangby.utils.threadpool;

import lombok.extern.slf4j.Slf4j;
import wang.wangby.annotation.monitor.TimeMonitor;
import wang.wangby.model.request.InvokeResult;
import wang.wangby.utils.LogUtil;
import wang.wangby.utils.threadpool.job.JobInfo;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ScheduledFactory {
	private static ScheduledFactory INSTANCE=new ScheduledFactory(); 

	private  Map<String, JobInfo> secheduleMap = new ConcurrentHashMap<>();

	/**执行计划任务
	 * @param  run 执行内容
	 * @param name  任务名称
	 * @param initialDelay 第一次执行时间
	 * @param period 执行间隔
	 * @param  unit 间隔单位
	 * */
	public static JobInfo newSchedule(Runnable run,String name,long initialDelay, long period, TimeUnit unit) {
		if (INSTANCE.secheduleMap.containsKey(name)) {
			throw new RuntimeException("计划任务名称已经存在:" + name);
		}
		log.debug("计划任务"+name+"将在"+unit.toSeconds(initialDelay)+"秒后执行,执行间隔:"+unit.toSeconds(period)+"(s)");
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(new ScheduledThreadFactory(name));
		JobInfo job=new JobInfo();
		job.setCreateInfo(LogUtil.getExceptionText(new Exception()));
		service.scheduleAtFixedRate(() -> {
			try {
				INSTANCE.call(job,run);
			} catch (Throwable e) {
				log.error("执行计划任务出错:"+e.getMessage(),e);
			}
		}, initialDelay, period, unit);
		job.setInitialDelayMs(unit.toMillis(initialDelay));
		job.setPeriodMs(unit.toMillis(period));
		job.setScheduledExecutorService(service);
		job.setRunning(true);
		job.setName(name);
		job.setCreateInfo(LogUtil.getExceptionText(new Exception("调用者堆栈")));
		INSTANCE.secheduleMap.put(name, job);
		return job;
	}

	public static  Map<String, JobInfo> secheduleMap(){
		return INSTANCE.secheduleMap;
	}

	//关闭计划某个计划任务
	public static void shutdown(String name){
		JobInfo job=INSTANCE.secheduleMap.get(name);
		if(job==null){
			return;
		}
		log.info("停止计划任务{}",name);
		INSTANCE.secheduleMap.remove(name);
		job.getScheduledExecutorService().shutdown();;
	}

	//暂停任务
	public static void pause(String name) {
		JobInfo job=INSTANCE.secheduleMap.get(name);
		if(job==null){
			return;
		}
		job.setRunning(false);
	}

	//恢复
	public static void resume(String name) {
		JobInfo job=INSTANCE.secheduleMap.get(name);
		if(job==null){
			return;
		}
		job.setRunning(true);
	}

	@TimeMonitor
	public  void call(JobInfo job,Runnable run) throws Exception {
		if(!job.isRunning()){
			return;
		}
		long begin=System.currentTimeMillis();
		job.begin();
		run.run();
		job.end();
		//打印执行耗时和已执行次数
		if(log.isDebugEnabled()){
			String info= InvokeResult.toJs(job.getName(),System.currentTimeMillis()-begin,job.getCount()+"");
			log.debug("[json]"+info);
		}
	}

	public static class ScheduledThreadFactory implements ThreadFactory {
		private final AtomicLong threadIndex = new AtomicLong(0);
		private final String threadNamePrefix;

		public ScheduledThreadFactory(final String threadNamePrefix) {
			this.threadNamePrefix = threadNamePrefix;
		}

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "scheduled_"+threadNamePrefix + this.threadIndex.incrementAndGet());
		}
	}
}
