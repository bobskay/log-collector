package wang.wangby.utils.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;


@Slf4j
//不需要返回的事件
abstract public class Event extends ThreadPoolEvent{

	abstract public void execute() throws Exception;

	@Override
	public Object call() throws Exception {
		this.execute();
		return null;
	}
}
