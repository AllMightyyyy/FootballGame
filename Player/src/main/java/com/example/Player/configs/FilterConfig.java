// src/main/java/com/example/Player/configs/FilterConfig.java
package com.example.Player.configs;

// Commenting out to avoid duplicate filter registration
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.example.Player.filter.JwtAuthenticationFilter;

@Configuration
public class FilterConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/api/*"); // Adjust based on your API routes
        return registrationBean;
    }
}
*/
