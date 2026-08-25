package com.forgeflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    // HTTP status code
    private int status;

    // Human-readable error message
    private String message;

    // Time when the error occurred
    private LocalDateTime timestamp;
}