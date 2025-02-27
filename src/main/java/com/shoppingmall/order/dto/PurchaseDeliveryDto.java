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
  private String receiverName;
  private String receiverPhone;
  private String receiverAddr;
  private String deliveryMessage;
  private String deliveryStatus;
  private LocalDateTime cancelAt; // 주문 취소 시간
  private LocalDateTime deliveredAt;
  private String userId;

  @Builder
  public PurchaseDeliveryDto(Long deliveryId, String reciverName, String reciverPhone, String reciverAddr,
                             String deliveryMessage, String deliveryStatus, LocalDateTime deliveredAt, String userId
                              ,LocalDateTime cancelAt) {
    this.deliveryId = deliveryId;
    this.receiverName = reciverName;
    this.receiverPhone = reciverPhone;
    this.receiverAddr = reciverAddr;
    this.deliveryMessage = deliveryMessage;
    this.deliveryStatus = deliveryStatus;
    this.deliveredAt = deliveredAt;
    this.userId = userId;
    this.cancelAt = cancelAt;
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
        .userId(delivery.getUserId())
        .cancelAt(delivery.getCancelAt())
        .build();
  }
}
