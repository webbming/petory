package com.shoppingmall.cart.model;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class CartItemDTO {
	 private Long id;
	    private Long productId;
	    private String productName;  // 추가된 부분
	    private String productImageUrl;  // 추가된 부분
	    private int quantity;
	    private BigDecimal price;

	    public CartItemDTO(CartItem cartItem) {
	        this.id = cartItem.getId();
	        this.productId = cartItem.getProduct().getProductId();
	        this.productName = cartItem.getProduct().getProductName();  // 상품명 추가
	        this.productImageUrl = cartItem.getProduct().getImageUrl();  // 상품 이미지 URL 추가
	        this.quantity = cartItem.getQuantity();
	        this.price = cartItem.getPrice();
	    }
}
