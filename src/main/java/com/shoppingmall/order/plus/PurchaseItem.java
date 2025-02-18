package com.shoppingmall.order.plus;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
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
public class PurchaseItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseItemId; // 주문 상품 ID (PK)

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id") // 주문 ID (FK)
//    private Purchase purchase;
    private Long orderId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id") // 상품 ID (FK)
    private Long productId;

    private String productName;

    private String option; // 상품 옵션

    private int quantity; // 수량
    
    private int price; // 가격

    private int totalPrice; // 해당 상품 총 가격
}
