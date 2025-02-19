package com.shoppingmall.order.dto;

import com.shoppingmall.order.domain.PurchaseDelivery;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PurchaseDeliveryDto {

  private Long deliveryId;
  private String reciverName;
  private String reciverPhone;
  private String reciverAddr;
  private String deliveryMessage;
  private String deliveryStatus;
  private LocalDateTime deliveredAt;

  @Builder
  public PurchaseDeliveryDto(Long deliveryId, String reciverName, String reciverPhone, String reciverAddr,
                             String deliveryMessage, String deliveryStatus, LocalDateTime deliveredAt) {
    this.deliveryId = deliveryId;
    this.reciverName = reciverName;
    this.reciverPhone = reciverPhone;
    this.reciverAddr = reciverAddr;
    this.deliveryMessage = deliveryMessage;
    this.deliveryStatus = deliveryStatus;
    this.deliveredAt = deliveredAt;
  }

  public static PurchaseDeliveryDto fromEntity(PurchaseDelivery delivery) {
    return PurchaseDeliveryDto.builder()
        .deliveryId(delivery.getDeliveryId())
        .reciverName(delivery.getReceiverName())
        .reciverPhone(delivery.getReceiverPhone())
        .reciverAddr(delivery.getReceiverAddr())
        .deliveryMessage(delivery.getDeliveryMessage())
        .deliveryStatus(delivery.getDeliveryStatus())
        .deliveredAt(delivery.getDeliveredAt())
        .build();
  }
}
