package com.weebeeio.demo.domain.login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignupRequestDto {
    private String id;
    private String password;
    private String nickname;
    private String name;
    private String gender;
    private Integer age;
    private String userRank; 
} 