package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PurchaseAllDto {

  private List<Purchase> purchase; // PurchaseList 리스트
  private List<PurchaseDelivery> purchaseDelivery; // PurchaseDelivery 리스트
  private List<PurchaseProduct> purchaseProduct; // PurchaseItem 리스트

  @Builder
  public PurchaseAllDto(List<Purchase> purchase, List<PurchaseDelivery> purchaseDelivery, List<PurchaseProduct> purchaseProduct) {
    this.purchase = purchase;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseProduct = purchaseProduct;
  }
}