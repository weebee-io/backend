package com.weebeeio.demo.domain.quest.controller;

import com.weebeeio.demo.domain.quest.service.QuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 출석 퀘스트 컨트롤러
 */
@Tag(name = "Quest", description = "출석 퀘스트 API")
@RestController
@RequestMapping("/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;

    /**

     * <p>퀘스트 화면에서 '출석하기' 버튼을 누르면 API를 호출</p>
     * <ul>
     *   <li>첫 호출 시: 코인 50개 지급 후 완료 메시지 반환</li>
     *   <li>재호출 시: 이미 오늘 출석했음을 알려주는 메시지 반환</li>
     * </ul>
     *
     * @param user 인증된 사용자 정보
     * @return 처리 결과 문자열
     */
    @Operation(summary = "출석 처리", 
               description = "오늘의 출석 완료 시 코인 50개를 지급")
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "성공적으로 처리됨"),
      @ApiResponse(responseCode = "400", description = "잘못된 인증 또는 로그인 정보")
    })
    @PostMapping("/attend")
    public ResponseEntity<String> attend(@org.springframework.security.core.annotation.AuthenticationPrincipal com.weebeeio.demo.domain.login.entity.User user) {
        int coin = questService.attend(user.getUserId());
        if (coin == 0) {
            return ResponseEntity.ok("이미 오늘 출석을 완료하셨습니다.");
        }
        return ResponseEntity.ok("출석 완료! 코인 +" + coin + " 지급되었습니다.");
    }
}
