package com.shoppingmall.user.dto;


import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserRoleType;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDTO {

  @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$" , message = "아이디 : 아이디는 특수문자를 제외한 4~20자리여야 합니다." )
  private String userId;
  @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호 : 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요." )
  private String password;
  @Email(message = "올바른 이메일 형식이 아닙니다." )
  private String email;
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임 : 닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
  private String nickname;
  @NotBlank(message = "질문 : 보안 질문을 선택하세요")
  private String question;
  @Pattern(regexp = "^[가-힣]{1,20}$" , message="답변 : 공백 없이 1~20자 이내 한글만 입력해주세요." )
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
