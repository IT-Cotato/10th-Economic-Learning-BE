package com.ripple.BE.global.dto.response;

import java.util.Collections;

import lombok.Builder;

@Builder
public record ApiResponse<T>(
	Boolean isSuccess,
	String code,
	String message,
	T results
) {
	public static final ApiResponse<Object> EMPTY_RESPONSE = ApiResponse.builder()
		.isSuccess(true)
		.code("REQUEST_OK")
		.message("요청이 승인되었습니다.")
		.results(Collections.EMPTY_MAP)
		.build();

	public static final ApiResponse<Object> JSON_ROLE_ERROR = ApiResponse.builder()
		.isSuccess(false)
		.code("JSON_ROLE_ERROR")
		.message("가진 권한으로는 실행할 수 없는 기능입니다.")
		.results(Collections.EMPTY_MAP)
		.build();

	public static final ApiResponse<Object> JSON_AUTH_ERROR = ApiResponse.builder()
		.isSuccess(false)
		.code("JSON_AUTH_ERROR")
		.message("로그인 후 다시 접근해주시기 바랍니다.")
		.results(Collections.EMPTY_MAP)
		.build();

	public static <T> ApiResponse<Object> from(T dto) {
		return ApiResponse.builder()
			.isSuccess(true)
			.code("REQUEST_OK")
			.message("request succeeded")
			.results(dto)
			.build();
	}
}
