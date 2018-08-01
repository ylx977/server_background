package com.gws.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/5.
 */
@WebFilter(urlPatterns = "/**")//也可以不要@WebFilter(urlPatterns = "/*")这个注解，另外在Application文件或者另外建立一个配置文件进行配置就可以了。
@Component
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
//        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, HEAD, OPTIONS, POST, PUT, DELETE");
//        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
//        httpServletResponse.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept,Content-Type, Authorization,Authorization, lang, Cookie");
//        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
//        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Content-Disposition, Set-Cookie");
        chain.doFilter(request,httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
