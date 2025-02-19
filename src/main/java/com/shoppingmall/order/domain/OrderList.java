package com.shoppingmall.order.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)  // Auditing을 위한 리스너 추가
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId; // 주문 ID (PK)

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "cart_id") // 장바구니 ID (FK)
//    private Cart cart;
    
    private String cartId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id") // 사용자 ID (FK)
//    private User user;
    
    private String userId;

    private int totalPrice; // 총 가격

    @CreatedDate// Auditing에 의한 날짜 자동 설정
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt; // 주문 생성 시간

    private LocalDateTime cancelAt; // 주문 취소 시간

    private String deliveryStatus; // 배송 상태

    private int discount; // 할인액

}
