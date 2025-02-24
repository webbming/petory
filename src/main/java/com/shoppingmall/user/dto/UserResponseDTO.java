package com.shoppingmall.user.dto;

import com.shoppingmall.user.model.UserRoleType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
  private String userId;
  private String email;
  private String nickname;
  private String address;
  private String accountType;
  private UserRoleType role;

}
