package com.sabu.hmacdemo.config;

import com.sabu.hmacdemo.exception.handler.TokenAuthenticationEntryPoint;
import com.sabu.hmacdemo.filter.HmacAuthenticationFilter;
import com.sabu.hmacdemo.restpath.RestPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/20
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CasSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .and()
                .cors()
                .and()
                .csrf()
                .disable()
                .headers()
                .xssProtection()
                .and()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(hmacAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl();
    }
    
    HmacAuthenticationFilter hmacAuthenticationFilter() throws Exception {
        HmacAuthenticationFilter hmacAuthenticationFilter = new HmacAuthenticationFilter(RestPath.HMAC_URLS);
        hmacAuthenticationFilter.setAuthenticationManager(authenticationManager());
        hmacAuthenticationFilter.setAuthenticationSuccessHandler(successHandler());
        hmacAuthenticationFilter.setAuthenticationFailureHandler(failureHandler());
        return hmacAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(normalAuthenticationProvider());
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
//        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    @Bean
    AuthenticationFailureHandler failureHandler() {
        return new AuthenticationEntryPointFailureHandler(authenticationEntryPoint());
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return new TokenAuthenticationEntryPoint();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
