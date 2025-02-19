package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.OrderList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PurchaseDto {

  private Long orderId; // 주문 ID
  private String cartId; // 장바구니 ID
  private String userId; // 사용자 ID
  private int totalPrice; // 총 가격
  private LocalDateTime createAt; // 주문 생성 시간
  private LocalDateTime cancelAt; // 주문 취소 시간
  private String deliveryStatus; // 배송 상태
  private int discount; // 할인액

  @Builder
  public PurchaseDto(Long orderId, String cartId, String userId, int totalPrice, LocalDateTime createAt,
                     LocalDateTime cancelAt, String deliveryStatus, int discount) {
    this.orderId = orderId;
    this.cartId = cartId;
    this.userId = userId;
    this.totalPrice = totalPrice;
    this.createAt = createAt;
    this.cancelAt = cancelAt;
    this.deliveryStatus = deliveryStatus;
    this.discount = discount;
  }

  // Entity -> DTO 변환 메서드
  public static PurchaseDto fromEntity(OrderList purchase) {
    return PurchaseDto.builder()
        .orderId(purchase.getOrderId())
        .cartId(purchase.getCartId())
        .userId(purchase.getUserId())
        .totalPrice(purchase.getTotalPrice())
        .createAt(purchase.getCreateAt())
        .cancelAt(purchase.getCancelAt())
        .deliveryStatus(purchase.getDeliveryStatus())
        .discount(purchase.getDiscount())
        .build();
  }
}