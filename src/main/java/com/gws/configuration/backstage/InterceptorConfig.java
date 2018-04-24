package com.gws.configuration.backstage;

import com.gws.interceptor.backstage.AuthorityInterceptor;
import com.gws.interceptor.backstage.TimeoutInterceptor;
import com.gws.interceptor.backstage.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    private final TokenInterceptor tokenInterceptor;

    private final TimeoutInterceptor timeoutInterceptor;

    private final AuthorityInterceptor authorityInterceptor;

    @Autowired
    public InterceptorConfig(TokenInterceptor tokenInterceptor, TimeoutInterceptor timeoutInterceptor, AuthorityInterceptor authorityInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
        this.timeoutInterceptor = timeoutInterceptor;
        this.authorityInterceptor = authorityInterceptor;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login");
        registry.addInterceptor(authorityInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login");
        registry.addInterceptor(timeoutInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login");

    }

}
