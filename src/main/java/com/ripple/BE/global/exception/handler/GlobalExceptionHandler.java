package com.ripple.BE.global.exception.handler;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ripple.BE.global.exception.errorcode.ErrorCode;
import com.ripple.BE.global.exception.errorcode.GlobalErrorCode;
import com.ripple.BE.global.exception.response.ErrorResponse;
import com.ripple.BE.global.exception.response.ErrorResponse.ValidationError;
import com.ripple.BE.global.exception.response.ErrorResponse.ValidationErrors;

import io.micrometer.common.lang.NonNull;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * 커스텀 예외 코드 예시
	 * @ExceptionHandler(PostNotFoundException.class)
	 *        public ResponseEntity<Object> handleReviewNotFound(PostNotFoundException e) {
	 * 		return handleExceptionInternal(e.getErrorCode());
	 *    }
	 */

	/**
	 * @Valid 관련 예외 처리 (DTO 검증 실패 시 발생)
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
	 * @param e 발생한 예외 객체
	 * @return 처리된 예외 응답
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllException(Exception e) {
		return handleExceptionInternal(GlobalErrorCode.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 예외 처리 결과를 생성하는 내부 메서드
	 * @param errorCode 처리할 에러 코드
	 * @return 생성된 ErrorResponse 객체
	 */
	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode));
	}

	/**
	 * ErrorResponse 객체를 생성하는 메서드
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
	 * @param e BindException 예외 객체
	 * @return 처리된 예외 응답
	 */
	private ResponseEntity<Object> handleExceptionInternal(BindException e) {
		return ResponseEntity.status(GlobalErrorCode.INVALID_PARAMETER.getHttpStatus())
			.body(makeErrorResponse(e));
	}

	/**
	 * BindException에서 발생한 유효성 오류를 ErrorResponse로 변환
	 * @param e BindException 예외 객체
	 * @return 생성된 ErrorResponse 객체
	 */
	private ErrorResponse makeErrorResponse(BindException e) {
		final List<ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ValidationError::from)
			.toList();

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
