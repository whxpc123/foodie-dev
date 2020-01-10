package com.imooc.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域设置配置
 */
@Configuration
public class CorsConfig {


    @Bean
    public CorsFilter corsFilter(){

        //添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://123.56.128.40:8080");
        config.addAllowedOrigin("http://123.56.128.40");

        //设置是否发送cookie信息
        config.setAllowCredentials(true);

        //设置允许请求的方式，例如get，post
        config.addAllowedMethod("*");

        //设置允许的header
        config.addAllowedHeader("*");

        //为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**",config);

        //返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }
}
