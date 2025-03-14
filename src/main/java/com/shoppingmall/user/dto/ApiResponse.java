package com.shoppingmall.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {

  private static final String SUCCESS_STATUS = "success";
  private static final String FAIL_STATUS = "fail";
  private static final String ERROR_STATUS = "error";

  private String status;
  private T data;
  private String message;

  // 성공 응답 (데이터 포함)
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(SUCCESS_STATUS, data, null);
  }

  // 성공 응답 (데이터 없음)
  public static <T> ApiResponse<T> success() {
    return success(null);
  }

  // 필드 검증 실패 응답
  public static <T> ApiResponse<T> validationError(T data) {
    return new ApiResponse<>(FAIL_STATUS, data, "필드 검증 오류");
  }

  // 일반 에러 응답 (데이터 포함)
  public static <T> ApiResponse<T> error(T data) {
    return new ApiResponse<>(ERROR_STATUS, data, null);
  }

  // 일반 에러 응답 (메시지만 포함)
  public static ApiResponse<Void> error(String message) {
    return new ApiResponse<>(ERROR_STATUS, null, message);
  }

  private ApiResponse(String status, T data, String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }
}
