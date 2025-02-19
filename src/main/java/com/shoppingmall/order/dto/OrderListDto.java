package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.Purchase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor //
public class OrderListDto {
  private Long orderId;
  private String productName;
  private int totalPrice;
}