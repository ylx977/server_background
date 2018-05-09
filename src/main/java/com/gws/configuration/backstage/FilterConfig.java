package com.gws.configuration.backstage;

import com.gws.filter.CORSFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ylx
 * Created by fuzamei on 2018/4/5.
 */
@Configuration
public class FilterConfig {

    private final CORSFilter corsFilter;

    @Autowired
    public FilterConfig(CORSFilter corsFilter) {
        this.corsFilter = corsFilter;
    }

    @Bean
    public FilterRegistrationBean filterRegister(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(corsFilter);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

}
