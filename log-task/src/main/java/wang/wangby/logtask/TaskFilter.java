package wang.wangby.logtask;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TaskFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        String url=((HttpServletRequest) servletRequest).getRequestURI();
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        if(!"/".equals(url)){
            ((HttpServletResponse) servletResponse).sendRedirect("/");
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
