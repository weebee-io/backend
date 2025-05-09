package com.weebeeio.demo.global.config;
// 예외 처리 핸들러
import com.weebeeio.demo.domain.login.dto.CommonResponseDto;
import com.weebeeio.demo.domain.login.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// user 예외 처리 핸들러
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<CommonResponseDto<Void>> handleUserException(UserException ex) {
        return ResponseEntity.ok(CommonResponseDto.error(ex.getMessage()));
    }
} 