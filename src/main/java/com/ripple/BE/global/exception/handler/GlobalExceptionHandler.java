package com.ripple.BE.global.exception.handler;

import com.ripple.BE.chatbot.exception.ChatbotException;
import com.ripple.BE.global.exception.errorcode.ErrorCode;
import com.ripple.BE.global.exception.errorcode.GlobalErrorCode;
import com.ripple.BE.global.exception.response.ErrorResponse;
import com.ripple.BE.global.exception.response.ErrorResponse.ValidationError;
import com.ripple.BE.global.exception.response.ErrorResponse.ValidationErrors;
import com.ripple.BE.image.exception.ImageException;
import com.ripple.BE.learning.exception.LearningException;
import com.ripple.BE.news.exception.NewsException;
import com.ripple.BE.notification.exception.NotificationException;
import com.ripple.BE.post.exception.PostException;
import com.ripple.BE.term.exception.TermException;
import com.ripple.BE.user.exception.UserException;
import io.micrometer.common.lang.NonNull;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 커스텀 예외 코드 예시 @ExceptionHandler(PostNotFoundException.class) public ResponseEntity<Object>
     * handleReviewNotFound(PostNotFoundException e) { return
     * handleExceptionInternal(e.getErrorCode()); }
     */

    /**
     * @Valid 관련 예외 처리 (DTO 검증 실패 시 발생)
     *
     * @param e MethodArgumentNotValidException 예외 객체
     * @param headers 요청 헤더
     * @param status HTTP 상태 코드
     * @param request WebRequest 객체
     * @return 처리된 예외 응답
     */
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException e,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        return handleExceptionInternal(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        return handleExceptionInternal(GlobalErrorCode.INVALID_PARAMETER);
    }

    /**
     * 모든 예외를 처리하는 기본 예외 처리기
     *
     * @param e 발생한 예외 객체
     * @return 처리된 예외 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception e, WebRequest request) {
        String contentType = request.getHeader("Accept");

        // SSE 요청일 경우 빈 응답 또는 연결 닫기 처리
        if (MediaType.TEXT_EVENT_STREAM_VALUE.equals(contentType)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        logger.error("예외 발생: {}", e);
        return handleExceptionInternal(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TermException.class)
    public ResponseEntity<Object> handleTermException(final TermException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Object> handleUserException(final UserException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(LearningException.class)
    public ResponseEntity<Object> handleLearningException(final LearningException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<Object> handlePostException(final PostException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(NewsException.class)
    public ResponseEntity<Object> handleNewsException(final NewsException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<Object> handleImageException(final ImageException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<Object> handleNotificationException(final NotificationException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(ChatbotException.class)
    public ResponseEntity<Object> handleChatbotException(final ChatbotException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    /**
     * 예외 처리 결과를 생성하는 내부 메서드
     *
     * @param errorCode 처리할 에러 코드
     * @return 생성된 ErrorResponse 객체
     */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode));
    }

    /**
     * ErrorResponse 객체를 생성하는 메서드
     *
     * @param errorCode 처리할 에러 코드
     * @return 생성된 ErrorResponse 객체
     */
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .results(new ValidationErrors(null))
                .build();
    }

    /**
     * BindException (DTO 검증 실패) 처리
     *
     * @param e BindException 예외 객체
     * @return 처리된 예외 응답
     */
    private ResponseEntity<Object> handleExceptionInternal(BindException e) {
        return ResponseEntity.status(GlobalErrorCode.INVALID_PARAMETER.getHttpStatus())
                .body(makeErrorResponse(e));
    }

    /**
     * BindException에서 발생한 유효성 오류를 ErrorResponse로 변환
     *
     * @param e BindException 예외 객체
     * @return 생성된 ErrorResponse 객체
     */
    private ErrorResponse makeErrorResponse(BindException e) {
        final List<ValidationError> validationErrorList =
                e.getBindingResult().getFieldErrors().stream().map(ValidationError::from).toList();

        return getBuild(validationErrorList);
    }

    private static ErrorResponse getBuild(List<ValidationError> validationErrorList) {
        return ErrorResponse.builder()
                .isSuccess(false)
                .code(GlobalErrorCode.INVALID_PARAMETER.name())
                .message(GlobalErrorCode.INVALID_PARAMETER.getMessage())
                .results(new ValidationErrors(validationErrorList))
                .build();
    }
}
