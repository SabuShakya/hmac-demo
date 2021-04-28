package com.sabu.hmacdemo.exception.handler;


import com.sabu.hmacdemo.dto.GenericResponse;
import com.sabu.hmacdemo.exception.NoContentFoundException;
import com.sabu.hmacdemo.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception : {}", ex);
        GenericResponse response = new GenericResponse(false, "System error");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedLoginException(UnauthorizedException ex) {
        log.error("Unauthorized exception : {}", ex);
        GenericResponse response = new GenericResponse(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NoContentFoundException.class)
    public ResponseEntity<Object> handleUnauthorizedLoginException(NoContentFoundException ex) {
        log.error("No Content Found Exception : {}", ex);
        GenericResponse response = new GenericResponse(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}
