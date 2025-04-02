package com.shoppingmall.order.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Long purchaseId; // 주문 ID (PK)

    @OneToMany(mappedBy = "purchase")
    private List<PurchaseProduct> purchaseProduct;

    @OneToOne(mappedBy = "purchase")
    private PurchaseDelivery purchaseDelivery;

    private String userId; // 사용자 ID (단순한 String)

//    @Column(nullable = false)
    private Integer totalPrice; // 총 가격

//    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt; // 주문 생성 시간

    private LocalDateTime cancelAt; // 주문 취소 시간

    private String deliveryStatus; // 배송 상태

    private String discount; // 할인액
}