package com.shoppingmall.order.plus;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)  // Auditing을 위한 리스너 추가
public class PurchaseDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId; // 배송 ID (PK)

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id") // 주문 ID (FK)
    private Long orderId;

    private String reciverName; // 수령인 이름

    private String reciverPhone; // 수령인 전화번호

    private String reciverAddr; // 수령인 주소

    private String deliveryMessage; // 배송 메시지

    private String deliveryStatus; // 배송 상태

    private LocalDateTime deliveredAt; // 배송 완료 시간

}

