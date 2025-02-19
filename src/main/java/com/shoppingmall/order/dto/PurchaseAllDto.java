package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import com.shoppingmall.order.domain.PurchaseDelivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PurchaseAllDto {

  private PurchaseList purchaseList; // PurchaseList 엔티티
  private PurchaseDelivery purchaseDelivery; // PurchaseDelivery 엔티티
  private List<PurchaseItem> purchaseItems; // PurchaseItem 엔티티 리스트

  @Builder
  public PurchaseAllDto(PurchaseList purchaseList, PurchaseDelivery purchaseDelivery, List<PurchaseItem> purchaseItems) {
    this.purchaseList = purchaseList;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseItems = purchaseItems;
  }
}
