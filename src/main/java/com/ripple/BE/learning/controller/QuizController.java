package com.ripple.BE.learning.controller;

import com.ripple.BE.global.dto.response.ApiResponse;
import com.ripple.BE.learning.application.quiz.QuizService;
import com.ripple.BE.learning.dto.response.quiz.QuizResponseDTO;
import com.ripple.BE.learning.dto.response.quiz.QuizResultResponseDTO;
import com.ripple.BE.learning.dto.response.quiz.RandomQuizResponseDTO;
import com.ripple.BE.user.domain.CustomUserDetails;
import com.ripple.BE.user.domain.type.Level;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/learning")
@Tag(name = "Quiz", description = "퀴즈 API")
public class QuizController {

    private final QuizService quizService;

    @Operation(
            summary = "퀴즈 시작",
            description = "퀴즈를 시작하기 전 호출해주세요. (틀린 문제 보기, 스크랩한 퀴즈 조회)에서는 호출하지 않습니다.")
    @PostMapping("/quizzes")
    public ResponseEntity<ApiResponse<Object>> startQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @RequestParam("learningSetId") long learningSetId,
            final @RequestParam(defaultValue = "BEGINNER") Level level) {

        List<RandomQuizResponseDTO> randomQuizResponseDTOList =
                quizService.startQuiz(currentUser.getId(), learningSetId, level);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(randomQuizResponseDTOList));
    }

    @Validated
    @Operation(
            summary = "퀴즈 제출",
            description = "퀴즈 한 문제를 풀고 나서 정답 여부와 해설을 반환합니다. 정답 선지 번호는 0부터 3까지입니다.")
    @PostMapping("/quizzes/{quizId}")
    public ResponseEntity<ApiResponse<Object>> submitAnswer(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("quizId") long quizId,
            @RequestParam @Min(0) @Max(3) Integer answerIndex) {

        QuizResultResponseDTO quizResultResponseDTO =
                quizService.submitAnswer(currentUser.getId(), quizId, answerIndex);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(quizResultResponseDTO));
    }

    @Operation(
            summary = "퀴즈 완료",
            description =
                    "퀴즈를 종료한 후 호출합니다. (틀린 문제 보기, 스크랩한 퀴즈 조회)에서는 호출하지 않습니다. 퀴즈를 시작한 후에 30 분이 지나면 퀴즈 정답률에 대한 사용자 통계를 저장할 수 없습니다.")
    @PostMapping("/quizzes/end")
    public ResponseEntity<ApiResponse<Object>> finishQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser) {

        quizService.finishQuiz(currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(summary = "퀴즈 스크랩", description = "퀴즈를 스크랩 처리합니다.")
    @PostMapping("/quiz/{quizId}/scrap")
    public ResponseEntity<ApiResponse<?>> scrapQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("quizId") long quizId) {

        quizService.scrapQuiz(currentUser.getId(), quizId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.EMPTY_RESPONSE);
    }

    @Operation(summary = "퀴즈 스크랩 취소", description = "퀴즈 스크랩을 취소합니다.")
    @DeleteMapping("/quiz/{quizId}/scrap")
    public ResponseEntity<ApiResponse<Object>> unscrapQuiz(
            final @AuthenticationPrincipal CustomUserDetails currentUser,
            final @PathVariable("quizId") long id) {

        quizService.removeScrapFromQuiz(id, currentUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(ApiResponse.EMPTY_RESPONSE));
    }

    @Operation(
            summary = "개별(오답, 스크랩) 퀴즈 조회",
            description = "퀴즈 id를 받아 퀴즈 상세 정보를 반환합니다. 오답 문제 보기, 스크랩한 퀴즈 보기 기능을 위한 API입니다.")
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<ApiResponse<Object>> getSingleQuiz(
            final @PathVariable("quizId") long quizId) {

        QuizResponseDTO quizResponseDTO = quizService.getSingleQuiz(quizId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(quizResponseDTO));
    }

    @Operation(
            summary = "저장한 퀴즈 다시 풀기",
            description = "저장한 퀴즈를 다시 풀기 위한 API입니다. 퀴즈 id와 답안 인덱스를 받아 정답 여부와 퀴즈 정보를 반환합니다.")
    @PostMapping("/quiz/{quizId}/retry")
    public ResponseEntity<ApiResponse<Object>> retryScrapQuiz(
            final @PathVariable("quizId") long quizId,
            @RequestParam @Min(0) @Max(3) Integer answerIndex) {
        QuizResultResponseDTO quizResultResponseDTO = quizService.retryScrapQuiz(quizId, answerIndex);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.from(quizResultResponseDTO));
    }
}
