package com.shoppingmall.cart.model;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shoppingmall.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;  // 개별 상품 가격
    
    @Column(nullable = false)
    private BigDecimal totalPrice;  // 총 가격 필드 추가
    
    @Version // 버전 관리 필드
    private Long version;
    
    public void updateTotalPrice() {
        this.totalPrice = BigDecimal.valueOf(this.quantity).multiply(this.price);
    }
    
    
    // 수량과 가격에 대한 유효성 검증 (선택 사항)
    public boolean isValid() {
    	return this.quantity > 0 && this.price.compareTo(BigDecimal.ZERO) > 0;
    }

    public CartItem(Cart cart, Product product, int quantity, BigDecimal price, BigDecimal totalPrice) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        updateTotalPrice();
    }
    
 // equals와 hashCode 메서드 추가
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(this.product.getProductId(),cartItem.product.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.product.getProductId());  // productId를 Product에서 가져옴
    }
    
}
