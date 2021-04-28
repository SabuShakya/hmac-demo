package com.sabu.hmacdemo.exception;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/25
 */
public class NoContentFoundException extends RuntimeException {
    public NoContentFoundException(String message) {
        super(message);
    }
}
