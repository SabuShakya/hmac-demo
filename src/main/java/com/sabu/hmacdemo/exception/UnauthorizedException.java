package com.sabu.hmacdemo.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
}
