package wang.wangby.utils.monitor;

import wang.wangby.utils.threadpool.Event;


public class RequestEvent extends Event{
	private TimeMonitor monitor;
	private String uri;
	private long consume;

	public RequestEvent(String uri,long consume,TimeMonitor monitor){
		this.monitor=monitor;
		this.uri=uri;
		this.consume=consume;
	}

	@Override
	public void execute() throws Exception{
		if(!monitor.getKeys().contains(uri)){
			synchronized(monitor.getKeys()){
				if(!monitor.getKeys().contains(uri)){
					TimeStatist rs=new TimeStatist(uri,monitor.getAccuracy());
					monitor.getMap().put(uri,rs);
					monitor.getKeys().add(uri);
				}
			}
		}
		TimeStatist rs=monitor.getMap().get(uri);
		rs.add(consume);
		monitor.getTotal().add(consume);
	}

}
