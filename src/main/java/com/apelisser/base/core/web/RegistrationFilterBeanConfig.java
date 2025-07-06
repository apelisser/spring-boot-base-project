package com.apelisser.base.core.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.apelisser.base.core.context.Context;

@Configuration
public class RegistrationFilterBeanConfig {

    private final Context context;

    public RegistrationFilterBeanConfig(Context context) {
        this.context = context;
    }

    @Bean
    public FilterRegistrationBean<ContextFilter> requestIdContextFilter() {
        FilterRegistrationBean<ContextFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ContextFilter(context));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
