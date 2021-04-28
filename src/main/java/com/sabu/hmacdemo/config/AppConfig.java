package com.sabu.hmacdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/27
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}