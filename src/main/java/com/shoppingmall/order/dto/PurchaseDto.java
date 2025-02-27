package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseDelivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PurchaseDto {

  private List<Purchase> purchase; // PurchaseList 리스트
  private List<PurchaseDelivery> purchaseDelivery; // PurchaseDelivery 리스트
  private List<PurchaseItem> purchaseItem; // PurchaseItem 리스트

  @Builder
  public PurchaseDto(List<Purchase> purchase, List<PurchaseDelivery> purchaseDelivery, List<PurchaseItem> purchaseItem) {
    this.purchase = purchase;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseItem = purchaseItem;
  }
}