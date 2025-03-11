package com.shoppingmall.cart.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
	private Long id;
    private List<CartItemDTO> cartItems;
    private BigDecimal totalPrice;
    
	    public CartDTO(Long id, List<CartItemDTO> cartItems) {
	        this.id = id;
	        this.cartItems = cartItems != null ? cartItems : new ArrayList<>();
	        // 총합 계산 (DTO에서 처리)
	        this.totalPrice = cartItems.stream()
	            .map(CartItemDTO::getTotalPrice)  // 각 상품의 totalPrice 가져오기
	            .reduce(BigDecimal.ZERO, BigDecimal::add);  // 합산
	    }
}
