package com.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryChangeDto {
    private Long purchaseProductId;

    private Long purchaseId;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddr;

    private String detailAddr;

    private String deliveryMessage;
}
