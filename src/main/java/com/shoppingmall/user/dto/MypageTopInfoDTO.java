package com.shoppingmall.user.dto;

import com.shoppingmall.user.model.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MypageTopInfoDTO {
    private String nickname;
    private int cartQuantity;
    private int couponCount;

}
