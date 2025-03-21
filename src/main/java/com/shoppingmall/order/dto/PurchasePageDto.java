package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor
public class PurchasePageDto {

  private Page<Purchase> purchase; // PurchaseList 리스트
  private Page<PurchaseDelivery> purchaseDelivery; // PurchaseDelivery 리스트
  private Page<PurchaseProduct> purchaseProduct; // PurchaseItem 리스트

  @Builder
  public PurchasePageDto(Page<Purchase> purchase, Page<PurchaseDelivery> purchaseDelivery, Page
      <PurchaseProduct> purchaseProduct) {
    this.purchase = purchase;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseProduct = purchaseProduct;
  }
}