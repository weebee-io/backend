package com.weebeeio.demo.domain.login.controller;

import com.weebeeio.demo.domain.login.dto.CommonResponseDto;
import com.weebeeio.demo.domain.login.dto.UserLoginRequestDto;
import com.weebeeio.demo.domain.login.dto.UserSignupRequestDto;
import com.weebeeio.demo.domain.login.entity.User;
import com.weebeeio.demo.domain.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Login", description = "사용자 관리 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 회원가입 : 토큰 없이 사용자 정보만 반환 */
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록한다.")
    public ResponseEntity<CommonResponseDto<User>> signup(@RequestBody UserSignupRequestDto requestDto) {
        User user = userService.signup(requestDto);
        return ResponseEntity.ok(CommonResponseDto.success(user, "회원가입이 완료되었습니다."));
    }

    /** 로그인 : 토큰(String)만 반환 */
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인을 처리하여 JWT 토큰을 발급한다.")
    public ResponseEntity<CommonResponseDto<String>> login(@RequestBody UserLoginRequestDto requestDto) {
        String token = userService.login(requestDto);
        return ResponseEntity.ok(CommonResponseDto.success(token, "로그인이 완료되었습니다."));
    }

    /** 이하 보호된 API — JWT 토큰 필요 */
    @GetMapping("/{userId}")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회한다.")
    public ResponseEntity<CommonResponseDto<User>> getUserInfo(
            @PathVariable Integer userId) {
        User user = userService.getUserInfo(userId);
        return ResponseEntity.ok(
            CommonResponseDto.success(user, "회원 정보 조회가 완료되었다.")
        );
    }

    @PutMapping("/{userId}")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정한다.")
    public ResponseEntity<CommonResponseDto<User>> updateUserInfo(
            @PathVariable Integer userId,
            @RequestBody UserSignupRequestDto requestDto) {
        User user = userService.updateUserInfo(userId, requestDto);
        return ResponseEntity.ok(
            CommonResponseDto.success(user, "회원 정보 수정이 완료되었다.")
        );
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 처리한다.")
    public ResponseEntity<CommonResponseDto<Void>> deleteUser(
            @PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(
            CommonResponseDto.success(null, "회원 탈퇴가 완료되었다.")
        );
    }

    @GetMapping("/check-id/{id}")
    @Operation(summary = "아이디 중복 체크", description = "아이디 중복 여부를 확인한다.")
    public ResponseEntity<CommonResponseDto<Boolean>> checkIdDuplicate(
            @PathVariable String id) {
        boolean isDuplicate = userService.checkIdDuplicate(id);
        return ResponseEntity.ok(
            CommonResponseDto.success(
                isDuplicate,
                isDuplicate ? "이미 사용 중인 아이디이다." : "사용 가능한 아이디이다."
            )
        );
    }
}
