package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseAllDto2 {

  private PurchaseList purchaseList;
  private PurchaseDelivery purchaseDelivery;
  private PurchaseItem purchaseItem;
  private String formattedCreateAt; // 포맷팅된 주문 시간

  @Builder
  public PurchaseAllDto2(PurchaseList purchaseList, PurchaseDelivery purchaseDelivery, PurchaseItem purchaseItem, String formattedCreateAt) {
    this.purchaseList = purchaseList;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseItem = purchaseItem;
    this.formattedCreateAt = formattedCreateAt;
  }
}