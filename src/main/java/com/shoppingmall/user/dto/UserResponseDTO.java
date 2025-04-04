package com.shoppingmall.user.dto;

import com.shoppingmall.user.model.UserRoleType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

  private long id;
  private String userId;
  private String email;
  private String nickname;
  private String address;
  private String accountType;
  private String createdAt;
  private UserRoleType role;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class MypageInfo{
    private String nickname;
    private int cartQuantity;
    private int couponCount;
    private int onDeliveryStatusCount;
    private String url;
  }

}
