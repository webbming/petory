package com.shoppingmall.cart.model;

import java.math.BigDecimal;

import com.shoppingmall.product.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
    private Long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
//    @Column(name = "product_id", nullable = false)
//    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;  // 개별 상품 가격
    
    @Version // 버전 관리 필드
    private Long version;
    
    @Transient
    public BigDecimal getTotalPrice() {
        return  BigDecimal.valueOf(this.quantity).multiply(this.price);  // 총 가격 계산
    }
    
    public CartItem(Cart cart, Product product, int quantity, BigDecimal price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
    
//    // 수량과 가격에 대한 유효성 검증 (선택 사항)
//    public boolean isValid() {
//        return this.quantity > 0 && this.price > 0;  // 유효한 수량과 가격만 허용
//    }
}
