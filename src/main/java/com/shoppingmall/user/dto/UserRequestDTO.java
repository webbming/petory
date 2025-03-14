package com.shoppingmall.user.dto;

import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class UserRequestDTO {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Create {
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{3,20}$", message = "아이디 : 영문, 숫자로 4~20자여야 합니다.")
    private String userId;

    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,20}$",
        message = "비밀번호 : 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Pattern(
        regexp = "^[a-zA-Z0-9_+&*-]{1,30}@[A-Za-z0-9-]{1,20}\\.[A-Za-z]{2,10}$",
        message = "이메일 : 이메일 형식을 올바르게 입력해주세요.")
    private String email;

    @Pattern(
        regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$",
        message = "닉네임 : 닉네임은 특수문자를 제외한 2~10자로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "질문 : 보안 질문을 선택해주세요.")
    private String question;

    @Pattern(regexp = "^[가-힣]{2,20}$", message = "답변 : 한글 1~20자로 입력해주세요.")
    private String answer;

    @NotBlank(message = "주소 : 주소를 검색해주세요")
    private String address;

    /* DTO -> Entity */
    public User toEntity() {

      return User.builder()
          .userId(userId)
          .password(password)
          .email(email)
          .nickname(nickname)
          .question(question)
          .answer(answer)
          .address(address)
          .role(UserRoleType.USER)
          .build();
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Update {

    @Pattern(
        regexp = "^[a-zA-Z0-9_+&*-]{1,30}@[A-Za-z0-9-]{1,20}\\.[A-Za-z]{2,10}$",
        message = "이메일 : 이메일 형식을 올바르게 입력해주세요.")
    private String email;

    @Pattern(
        regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$",
        message = "닉네임 : 닉네임은 특수문자를 제외한 2~10자로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "주소 : 주소를 검색해주세요")
    private String address;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UpdatProfile {

    private String nickname;
    private MultipartFile userImg;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Delete {
    private String password;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Check {

    private String fieldName;
    private String fieldValue;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FindID {
    private String question;
    private String answer;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FindPass {
    private String userId;
    private String email;
  }
}
