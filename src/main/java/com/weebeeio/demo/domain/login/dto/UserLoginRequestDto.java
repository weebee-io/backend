package com.weebeeio.demo.domain.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginRequestDto {
    private String id;
    private String password;
} 