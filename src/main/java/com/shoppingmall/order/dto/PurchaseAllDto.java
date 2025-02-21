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

  private List<PurchaseList> purchaseList; // PurchaseList 리스트
  private List<PurchaseDelivery> purchaseDelivery; // PurchaseDelivery 리스트
  private List<PurchaseItem> purchaseItem; // PurchaseItem 리스트

  @Builder
  public PurchaseAllDto(List<PurchaseList> purchaseList, List<PurchaseDelivery> purchaseDelivery, List<PurchaseItem> purchaseItem) {
    this.purchaseList = purchaseList;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseItem = purchaseItem;
  }
}