package com.shoppingmall.order;

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
@Data // Auditing을 위한 리스너 추가
public class Purchase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    
    @Column(nullable = false)
    private String reciverAddr;
    @Column(nullable = false)
    private String reciverPhone;
    @Column(nullable = false)
    private String reciverName;

    private String cartId;
    
    @Column(name = "user_id")
    private String userId;  // 필드 이름을 userId로 수정
    
    private String totalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    private LocalDateTime cancelAt;
    
}
