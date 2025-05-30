package com.weebeeio.demo.domain.login.service;

import com.weebeeio.demo.domain.login.dto.UserLoginRequestDto;
import com.weebeeio.demo.domain.login.dto.UserSignupRequestDto;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.exception.UserException;
import com.weebeeio.demo.domain.login.repository.UserRepository;
import com.weebeeio.demo.global.util.JwtUtil;


import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public User signup(UserSignupRequestDto requestDto) {
        try {
            // 아이디 중복 검사
            if (userRepository.existsById(requestDto.getId())) {
                throw new UserException("이미 존재하는 아이디입니다.");
            }

            // 회원 정보 생성
            User user = User.builder()
                    .id(requestDto.getId())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .nickname(requestDto.getNickname())
                    .name(requestDto.getName())
                    .gender(requestDto.getGender())
                    .age(requestDto.getAge())
                    .userrank((requestDto.getUserRank()))
                    .build();

            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 예외 출력
            throw e;
        }
    }

    @Override
    public String login(UserLoginRequestDto requestDto) {
        // 사용자 조회
        User user = userRepository.findById(requestDto.getId())
                .orElseThrow(() -> new UserException("존재하지 않는 아이디입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 토큰 생성 및 반환
        return jwtUtil.generateToken(user);
    }

    @Override
    public Optional getUserInfo(Integer userId) {
        // TODO Auto-generated method stub
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public User updateUserInfo(Integer userId, UserSignupRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("존재하지 않는 회원입니다."));
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            // 기존 비밀번호 삭제 및 새로운 비밀번호로 업데이트
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }
        if (requestDto.getNickname() != null) {
            // 닉네임 중복 검사
            if (userRepository.existsByNickname(requestDto.getNickname())) {
                throw new UserException("이미 존재하는 닉네임입니다.");
            }
            user.setNickname(requestDto.getNickname());
        }
        user.setName(requestDto.getName());
        user.setGender(requestDto.getGender());
        user.setAge(requestDto.getAge());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("존재하지 않는 회원입니다."));
        userRepository.delete(user);
    }

    @Override
    public boolean checkIdDuplicate(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkIdDuplicate'");
    }

    @Transactional
    public void save(User user){
            userRepository.save(user);
        }
}