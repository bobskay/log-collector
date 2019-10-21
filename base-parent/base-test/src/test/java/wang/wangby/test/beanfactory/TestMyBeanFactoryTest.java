package wang.wangby.test.beanfactory;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TestMyBeanFactoryTest {

    @Test
    public void get() {
        ClassC a=TestBeanFactory.get(ClassC.class);
        a.hello();
    }

    public static class ClassA{
        @Autowired
        private ClassB classB;

        public void hello(){
            log.debug("classA hello:"+classB.getC());
        }
    }

    public static class ClassB{
        @Autowired
        private ClassC classC;
        public String getC(){
            return classC.getName();
        }
    }

    public static class ClassC extends ClassA{
        public String getName(){
            return "当前类是:"+this.getClass().getSimpleName();
        }
    }
}