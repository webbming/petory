package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Builder
public class ProductAndDeliveryDto {

    private String message;
    private List<PurchaseProduct> purchaseProduct;
    private List<PurchaseDelivery> purchaseDelivery;

}
