package com.shoppingmall.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "couponList_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String couponName;

    @Column(nullable = false)
    private int discount;

    @Column(nullable = false, length = 100)
    private String couponComment;

    @Column(nullable = false)
    private LocalDateTime createAt;


    private LocalDateTime usedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id") // coupon_id FK
    private Coupon coupon; // 양방향 관계의 주인
}
