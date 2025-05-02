package com.weebeeio.demo.domain.login.service;

import com.weebeeio.demo.domain.login.dto.UserLoginRequestDto;
import com.weebeeio.demo.domain.login.dto.UserSignupRequestDto;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.exception.UserException;
import com.weebeeio.demo.domain.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User signup(UserSignupRequestDto requestDto) {
        // 아이디 중복 검사
        if (userRepository.existsById(requestDto.getId())) {
            throw new UserException("이미 존재하는 아이디입니다.");
        }

        // 회원 정보 생성
        User user = User.builder()
                .id(requestDto.getId())
                .password(requestDto.getPassword()) // TODO: 비밀번호 암호화 필요
                .nickname(requestDto.getNickname())
                .name(requestDto.getName())
                .gender(requestDto.getGender())
                .age(requestDto.getAge())
                .userrank((requestDto.getUserRank()))
                .userSegment(requestDto.getUserSegment())
                .build();

        return userRepository.save(user);
    }

    @Override
    public String login(UserLoginRequestDto requestDto) {
        // 로그인 검증
        User user = userRepository.findByIdAndPassword(requestDto.getId(), requestDto.getPassword())
                .orElseThrow(() -> new UserException("아이디 또는 비밀번호가 일치하지 않습니다."));

        // TODO: JWT 토큰 생성 및 반환
        return "로그인 성공"; // 임시 반환값
    }

    @Override
    public User getUserInfo(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException("존재하지 않는 회원입니다."));
    }

    @Override
    @Transactional
    public User updateUserInfo(Integer userId, UserSignupRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("존재하지 않는 회원입니다."));

        // TODO: 비밀번호 변경 시 암호화 필요
        User updatedUser = User.builder()
                .userId(userId)
                .id(user.getId()) // 아이디는 변경 불가
                .password(requestDto.getPassword())
                .nickname(requestDto.getNickname())
                .name(requestDto.getName())
                .gender(requestDto.getGender())
                .age(requestDto.getAge())
                .userrank((requestDto.getUserRank()))
                .userSegment(requestDto.getUserSegment())
                .build();

        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserException("존재하지 않는 회원입니다.");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public boolean checkIdDuplicate(String id) {
        return userRepository.existsById(id);
    }
} 