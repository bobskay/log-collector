package wang.wangby.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class MyBeanFactory {
    private static MyBeanFactory INSTANCE;

    public static <T> T getBean(Class<T> clazz) {
        return INSTANCE.getBeanInternal(clazz);
    }

    public static void setFactory(MyBeanFactory myBeanFactory) {
        log.debug("初始化beanfactory:" + myBeanFactory);
        INSTANCE = myBeanFactory;
    }

    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        return INSTANCE.getBeansOfTypeInternal(clazz);
    }

    protected <T> Collection<T> getBeansOfTypeInternal(Class<T> clazz) {
        throw new RuntimeException("unimpl");
    }

    protected <T> T getBeanInternal(Class<T> clazz) {
        return ClassUtil.newInstance(clazz);
    }
}
