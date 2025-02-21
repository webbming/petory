package com.shoppingmall.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor //
public class PurchaseListDto {

  private Long orderId;
  private String productName;
  private int totalPrice;

  public PurchaseListDto(Long orderId, String productName, int totalPrice) {
    this.orderId = orderId;
    this.productName = productName;
    this.totalPrice = totalPrice;
  }
}