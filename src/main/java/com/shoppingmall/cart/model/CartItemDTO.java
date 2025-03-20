package com.shoppingmall.cart.model;

import java.math.BigDecimal;
import java.util.List;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;
    private BigDecimal totalPrice;


    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.productId = cartItem.getProduct().getProductId();
        this.productName = cartItem.getProduct().getProductName();
        List<String> imageUrls = cartItem.getProduct().getImageUrls();
        this.imageUrl = (imageUrls != null && !imageUrls.isEmpty()) ? imageUrls.get(0) : null;
        this.price = cartItem.getPrice();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));
    }

}