package com.sabu.hmacdemo.exception.handler;

import com.sabu.hmacdemo.dto.GenericResponse;
import com.sabu.hmacdemo.util.JacksonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/20
 */
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static HttpMessageConverter<String> messageConverter = new StringHttpMessageConverter();

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException authException) throws IOException, ServletException {
        GenericResponse genericResponse = new GenericResponse(false, authException.getMessage());
        ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
        outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

        messageConverter.write(JacksonUtil.getString(genericResponse), MediaType.APPLICATION_JSON, outputMessage);

    }
}
