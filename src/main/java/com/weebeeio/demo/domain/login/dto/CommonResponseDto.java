package com.weebeeio.demo.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> CommonResponseDto<T> success(T data, String message) {
        return CommonResponseDto.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponseDto<T> error(String message) {
        return CommonResponseDto.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
} 