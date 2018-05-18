package com.gws.configuration.backstage;

import com.gws.interceptor.backstage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/18.
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    private final TokenInterceptor tokenInterceptor;

    private final TimeoutInterceptor timeoutInterceptor;

    private final AuthorityInterceptor authorityInterceptor;

    private final LangInterceptor langInterceptor;

    private final FreezeInterceptor freezeInterceptor;

    @Autowired
    public InterceptorConfig(TokenInterceptor tokenInterceptor, TimeoutInterceptor timeoutInterceptor, AuthorityInterceptor authorityInterceptor, LangInterceptor langInterceptor, FreezeInterceptor freezeInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
        this.timeoutInterceptor = timeoutInterceptor;
        this.authorityInterceptor = authorityInterceptor;
        this.langInterceptor = langInterceptor;
        this.freezeInterceptor = freezeInterceptor;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(langInterceptor)
                .addPathPatterns("/api/**");
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login")
                .excludePathPatterns("/api/backstage/fastLogin");
        registry.addInterceptor(authorityInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login")
                .excludePathPatterns("/api/backstage/fastLogin");
        registry.addInterceptor(timeoutInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login")
                .excludePathPatterns("/api/backstage/fastLogin");
        registry.addInterceptor(freezeInterceptor)
                .addPathPatterns("/api/backstage/**")
                .excludePathPatterns("/api/backstage/login")
                .excludePathPatterns("/api/backstage/fastLogin");

    }

}
