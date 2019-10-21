package wang.wangby.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

//ch.qos.logback.core.ConsoleAppender
public class LogEventAppender extends UnsynchronizedAppenderBase {
    private Queue queue = new LinkedBlockingQueue();
    protected Encoder encoder;


    @Override
    protected void append(Object eventObject) {
        byte[] bs=encoder.encode(eventObject);
        System.out.println("lalal"+new String(bs));
    }

    public void start() {
        System.out.println("启动" + encoder);
        started = true;
    }

    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }
}
