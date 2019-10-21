package wang.wangby.utils.monitor;

import wang.wangby.utils.threadpool.ThreadPool;
import wang.wangby.utils.threadpool.ThreadPoolFactory;

import java.util.*;
import java.util.Map.Entry;

//监控某个执行耗时
public class TimeMonitor implements Monitor{
	private ThreadPool threadPool;
	private Map<String,TimeStatist> map=Collections.synchronizedMap(new HashMap());
	private Set<String> keys=new HashSet();
	private TimeStatist total;
	private long[] accuracy;

	//name,名称
	//accuracy 统计唯独
	//coreSize,统计用的线程数
	public TimeMonitor(String name,long[] accuracy,int coreSize){
		this.accuracy=Arrays.copyOf(accuracy,accuracy.length);
		total=new TimeStatist(name+".totalconsume",Arrays.copyOf(accuracy,accuracy.length));
		map.put(name+".totalrequest",total);
		threadPool=ThreadPoolFactory.newPool(name,coreSize);
	}

	/**
	 * 新增一次请求
	 * 
	 * @param uri 请求的uri
	 * @prram consume 耗时
	 */
	public void addRequest(String uri,long consume){
		RequestEvent event=new RequestEvent(uri,consume,this);
		threadPool.execute(event);
	}


	public TimeStatist get(String name){
		return map.get(name);
	}

	public Set<String> getKeys(){
		return keys;
	}

	public long[] getAccuracy(){
		return this.accuracy;
	}

	public Map<String,TimeStatist> getMap(){
		return map;
	}

	public TimeStatist getTotal(){
		return total;
	}

	@Override
	public Map<String, Object> getResult() {
		Set<Entry<String,TimeStatist>> en=map.entrySet();
		Map map=Collections.synchronizedMap(new LinkedHashMap());
		for(Entry<String,TimeStatist> e:en){
			map.putAll(e.getValue().statist());
		}
		return map;
	}

}
