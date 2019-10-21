package wang.wangby.utils.threadpool;

import wang.wangby.utils.threadpool.ThreadPool.PoolInfo;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *创建线程池工具类,使用方法
 *ThreadPoolFactory.newPool("foo",10);
 */
public class ThreadPoolFactory{
	private  static ThreadPoolFactory INSTANCE=new ThreadPoolFactory(5);
	// 线程空闲多久后删除
	private int keepAliveSecond;
	//系统所有创建的线程池
	private  Map<String,ThreadPool> map=Collections.synchronizedMap(new HashMap());
	private ThreadPoolFactory(int keepAliveSecond){
		this.keepAliveSecond=keepAliveSecond;
	}

	/**
	 * @param name 线程池名字,必须全局唯一
	 * @param corePoolSize 线程池大小,
	 * 	平时线程数为corePoolSize,最多运行corePoolSize*2,超过峰值后允许再排队corePoolSize个
	 * 	过了高峰,如果线程空闲超过KEEP_ALIVE_SECOND,就会自动销毁恢复到corePoolSize
	 * */
	public static ThreadPool newPool(String name,int corePoolSize){
		return newPool(name,corePoolSize,INSTANCE.keepAliveSecond);
	}

	public static ThreadPool newPool(Class holder,int corePoolSize){
		return newPool(holder.getName(),corePoolSize,INSTANCE.keepAliveSecond);
	}

	synchronized public static ThreadPool newPool(String name,int corePoolSize,int keepAliveTime){
		if(INSTANCE.map.containsKey(name)){
			throw new RuntimeException("线程池名称已经被占用:"+name);
		}
		int maximumPoolSize=corePoolSize*2;
		int queue=corePoolSize;
		ThreadPool pool=new ThreadPool(corePoolSize,maximumPoolSize,keepAliveTime,queue,name );
		INSTANCE.map.put(name,pool);
		return pool;
	}

	/** 获取线程池运行情况 */
	public static Map  monitor(){
		Map result=Collections.synchronizedMap(new LinkedHashMap());
		INSTANCE.map.forEach((name,pool)->{
			PoolInfo info=pool.getInfo();
			result.put(name+"_"+"activeCount",info.getActiveCount());
			result.put(name+"_"+"completedTaskCount",info.getCompletedTaskCount());
			result.put(name+"_"+"largestPoolSize",info.getLargestPoolSize());
			result.put(name+"_"+"poolSize",info.getPoolSize());
			result.put(name+"_"+"queueSize",info.getQueueSize());
			result.put(name+"_"+"rejected",info.getRejected());
			result.put(name+"_"+"taskCount",info.getTaskCount());
		});
		return result;
	}
}
