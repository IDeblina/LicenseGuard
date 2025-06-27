package com.project.licenseservice.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private int stausCode;
    private String message;
    private String timestamp;
}
