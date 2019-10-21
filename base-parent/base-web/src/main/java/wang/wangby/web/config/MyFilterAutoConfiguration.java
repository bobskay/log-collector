package wang.wangby.web.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import wang.wangby.utils.StringUtil;
import wang.wangby.web.webfilter.MyFilter;
import wang.wangby.web.webfilter.QueueFilter;
import wang.wangby.web.webfilter.StatistFilter;
import wang.wangby.web.webfilter.stack.InvokeStackFilter;

import java.util.*;

@Configuration
@ConditionalOnClass(MyFilter.class)
@Slf4j
@Data
public class MyFilterAutoConfiguration {

    @Value("eureka.instance.instance-id")
    private String instanceId;

    @Bean
    @ConfigurationProperties(prefix = "my.webfilter")
    public MyFilterProperties myFilterProperties() {
        return new MyFilterProperties();
    }


    @Bean
    public MyFilter myFilter(MyFilterProperties myFilterProperties, RequestMappingHandlerMapping mappingHandlerMapping) {
        log.debug("创建自定义过滤器:"+myFilterProperties);
        List<String> exclusions = getFormatUrl(myFilterProperties.getExclusions());
        List webFilters = new ArrayList();
        if (myFilterProperties.invokeStack) {
            InvokeStackFilter invokeStackFilter = new InvokeStackFilter(myFilterProperties.stackIgnoreNs);
            webFilters.add(invokeStackFilter);
            log.info("开启堆栈打印");
        }

        if (myFilterProperties.statis) {
            List<String> igs = getFormatUrl(myFilterProperties.ignoreLog);
            webFilters.add(new StatistFilter(myFilterProperties.getMonitorPage(), igs));
            log.info("开启耗时统计");
        }
        if (myFilterProperties.queue && myFilterProperties.limit != null && myFilterProperties.limit.size() > 0) {
            QueueFilter f = new QueueFilter(myFilterProperties.getLimitMap());
            webFilters.add(f);
            log.info("开启请求排队");
        }

        log.debug("创建自定义过滤器:" + myFilterProperties);

        //只过滤配置了RequestMapping的链接
        Map<RequestMappingInfo, HandlerMethod> map = mappingHandlerMapping.getHandlerMethods();
        TreeSet set = new TreeSet();//所有可用的url
        for (RequestMappingInfo info : map.keySet()) {
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            set.addAll(patterns);
        }
        log.debug("自定义过滤器可处理的url:"+set);
        MyFilter filter = new MyFilter(webFilters, url -> {
            if(myFilterProperties.getMonitorPage().equalsIgnoreCase(url)){
                return true;
            }
            return set.contains(url);
        }, instanceId);
        return filter;
    }



    private List<String> getFormatUrl(String url) {
        List list = new ArrayList();
        url = url.trim();
        if (StringUtil.isEmpty(url)) {
            return list;
        }
        for (String s : url.split(",")) {
            String x = s.trim();
            if (StringUtil.isNotEmpty(x)) {
                if (!x.startsWith("/")) {
                    x = "/" + x;
                }
                list.add(x);
            }
        }
        return list;
    }
}
