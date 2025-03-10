package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseProduct;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Builder
public class PurchaseProductDto {

  private Long productId; // 상품 ID
  private String productName; // 상품명
  private String option; // 상품 옵션
  private int quantity; // 수량
  private int price; // 가격
  private String userId;
  private String deliveryStatus;
  private LocalDateTime createAt;
  private LocalDateTime cancelAt;

}