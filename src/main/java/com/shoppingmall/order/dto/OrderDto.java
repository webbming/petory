package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderDto {

  private List<PurchaseList> purchaseList; // PurchaseList 리스트
  private List<PurchaseDelivery> purchaseDelivery; // PurchaseDelivery 리스트
  private List<PurchaseItem> purchaseItems; // PurchaseItem 리스트
  private String formattedCreateAt; // 포맷팅된 날짜

  @Builder
  public OrderDto(List<PurchaseList> purchaseList, List<PurchaseDelivery> purchaseDelivery, List<PurchaseItem> purchaseItems, String formattedCreateAt) {
    this.purchaseList = purchaseList;
    this.purchaseDelivery = purchaseDelivery;
    this.purchaseItems = purchaseItems;
    this.formattedCreateAt = formattedCreateAt;
  }
}