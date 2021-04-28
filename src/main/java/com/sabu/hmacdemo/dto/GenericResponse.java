package com.sabu.hmacdemo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/20
 */
@Getter
@Setter
@NoArgsConstructor
public class GenericResponse {

    private boolean success;
    private String message;
    private Object data;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
