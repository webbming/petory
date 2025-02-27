package com.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
public class PurchaseProdeuctDto {

  private Long purchaseProductId; // 주문 상품 ID (PK)
  private Long productId; // 상품 ID
  private String productName; // 상품명
  private String option; // 상품 옵션
  private int quantity; // 수량
  private int price; // 가격
  private int totalPrice; // 해당 상품 총 가격



}
