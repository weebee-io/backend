package com.weebeeio.demo.domain.login.service;

import com.weebeeio.demo.domain.login.dto.UserLoginRequestDto;
import com.weebeeio.demo.domain.login.dto.UserSignupRequestDto;
import com.weebeeio.demo.domain.login.entity.User;

public interface UserService {
    // 회원가입
    User signup(UserSignupRequestDto requestDto);
    
    // 로그인
    String login(UserLoginRequestDto requestDto);
    
    // 회원 정보 조회
    User getUserInfo(Integer userId);
    
    // 회원 정보 수정
    User updateUserInfo(Integer userId, UserSignupRequestDto requestDto);
    
    // 회원 탈퇴
    void deleteUser(Integer userId);
    
    // 아이디 중복 확인
    boolean checkIdDuplicate(String id);
} 