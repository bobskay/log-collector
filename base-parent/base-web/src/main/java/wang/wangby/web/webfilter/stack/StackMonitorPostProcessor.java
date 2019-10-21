package wang.wangby.web.webfilter.stack;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import wang.wangby.utils.monitor.MonitorUtil;

import java.lang.reflect.Method;
import java.util.function.Function;

@Slf4j
public class StackMonitorPostProcessor implements BeanPostProcessor {

    public Function<Object,Boolean> logEnabled;
    public StackMonitorPostProcessor(Function<Object,Boolean> logEnabled){
        this.logEnabled=logEnabled;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!logEnabled.apply(bean)){
            return bean;
        }
        log.debug("记录:"+beanName);
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvice(new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                Method method = invocation.getMethod();
                String name=beanName+"."+method.getName();
                MonitorUtil.begin(name);
                try {
                    return invocation.proceed();
                } finally {
                    MonitorUtil.end(name);
                }
            }
        });
        return proxyFactory.getProxy();
    }
}
