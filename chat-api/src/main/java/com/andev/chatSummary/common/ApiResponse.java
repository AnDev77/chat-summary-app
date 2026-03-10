package com.andev.chatSummary.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

// src/main/java/com/project/chatsummary/common/ApiResponse.java
@Getter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final T data;

    public static <T> ApiResponse <T> ok(String message, T data) {
        return ApiResponse.<T>builder() // builder의 함수와 형맞춤.
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }
    public static <T> ApiResponse<T> fail(String message){
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(null)
                .build();
    }
}