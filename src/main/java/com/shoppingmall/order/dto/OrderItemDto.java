package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.OrderItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor //기본 생성자
public class PurchaseItemDto {

  private Long purchaseItemId;
  private Long productId;
  private String productName;
  private String option;
  private int quantity;
  private int price;
  private int totalPrice;

  @Builder
  public PurchaseItemDto(Long purchaseItemId, Long productId, String productName, String option, int quantity, int price, int totalPrice) {
    this.purchaseItemId = purchaseItemId;
    this.productId = productId;
    this.productName = productName;
    this.option = option;
    this.quantity = quantity;
    this.price = price;
    this.totalPrice = totalPrice;
  }

  public static PurchaseItemDto fromEntity(OrderItem item) {
    return PurchaseItemDto.builder()
        .purchaseItemId(item.getPurchaseItemId())
        .productId(item.getProductId())
        .productName(item.getProductName())
        .option(item.getOption())
        .quantity(item.getQuantity())
        .price(item.getPrice())
        .totalPrice(item.getTotalPrice())
        .build();
  }
}