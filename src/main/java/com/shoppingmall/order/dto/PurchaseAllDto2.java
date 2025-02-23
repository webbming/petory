package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.Purchase;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseAllDto2 {

  private Purchase purchase;
  private PurchaseDelivery purchaseDelivery;
  private PurchaseItem purchaseItem;
  private String formattedCreateAt; // 포맷팅된 주문 시간

  @Builder
  public PurchaseAllDto2(Purchase purchase, PurchaseDelivery purchaseDelivery, PurchaseItem purchaseItem, String formattedCreateAt) {
    this.purchase = purchase;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseItem = purchaseItem;
    this.formattedCreateAt = formattedCreateAt;
  }
}