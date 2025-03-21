package com.shoppingmall.order.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Builder
public class PurchaseReadyDto {

  private String imageUrl;
  private String productId; // 상품 ID
  private String productName; // 상품명
  private String quantity; // 수량
  private String price; // 가격
  private String userId;
}