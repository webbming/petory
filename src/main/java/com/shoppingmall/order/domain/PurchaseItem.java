package com.shoppingmall.order.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class PurchaseItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseItemId; // 주문 상품 ID (PK)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id") // 주문 ID (FK)
    private Purchase purchase;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id") // 상품 ID (FK)
    private Long productId;

    private String userId;

    private String productName;

    private String option; // 상품 옵션

    private int quantity; // 수량
    
    private int price; // 가격

//    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt; // 주문 생성 시간

    private LocalDateTime cancelAt; // 주문 취소 시간

    private int totalPrice; // 해당 상품 총 가격
}
