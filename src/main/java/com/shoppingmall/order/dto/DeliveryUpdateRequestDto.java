package com.shoppingmall.order.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryUpdateRequestDto {
  private Long purchaseProductId;
  private Long purchaseId;
  private String receiverName;
  private String receiverPhone;
  private String receiverAddr;
  private String receiverAddrDetail;
  private String deliveryMessage;
}
