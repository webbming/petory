package com.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MypageTopInfoDTO {
    private String nickname;
    private int cartQuantity;
    private int couponCount;

}
