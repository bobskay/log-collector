package wang.wangby.utils.monitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class MonitorFacade{
	public static final String HTTP_MONITOR="httpRequest";
	public static final String QUEUE_MONITOR="queueMonitor";
	
	private static Map<String,Monitor> monitors=Collections.synchronizedMap(new HashMap());
	public static final String LINE=System.getProperty("line.separator");
	//默认统计粒度
	public static final long[] DEFLUT_ACCURACY=new long[]{5,10,100,500,1000,5000};

	/**
	 * 获得一个处理耗时监视器
	 * 
	 * @param accuracy 统计粒度(ma)
	 */
	public static TimeMonitor timeMonitor(String name,long...accuracy){
		if(monitors.containsKey(name)) {
			return (TimeMonitor) monitors.get(name);
		}
		TimeMonitor m=new TimeMonitor(name,accuracy,10);
		monitors.put(name, m);
		return m;
	}
	
	public static Monitor getMonitor(String name) {
		return monitors.get(name);
	}

	public static String statist(){
		StringBuilder sb=new StringBuilder();
		for(Monitor m:monitors.values()){
			m.getResult().forEach((key,value)->{
				sb.append(key+"="+value);
				sb.append(LINE);
			});
			
		}
		sb.append("current:"+System.currentTimeMillis());
		sb.append(LINE);
		return sb.toString();
	}

	public static CountMonitor countMonior(String name){
		CountMonitor m=new CountMonitor(name);
		addMonior(name, m);
		return m;
	}

	public static void addMonior(String name,Monitor monitor){
		addMonior(name,monitor,false);
	}

	public static void addMonior(String name,Monitor monitor,boolean replace){
		if(monitors.containsKey(name)&&!replace) {
			throw new RuntimeException("名称已被占用:"+name);
		}
		monitors.put(name,monitor);
	}

	public static Map<String, Monitor> getMonitors() {
		return monitors;
	}
	
	
	
}
