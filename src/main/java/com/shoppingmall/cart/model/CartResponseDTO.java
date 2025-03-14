package com.shoppingmall.cart.model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CartResponseDTO {
	private Long id;
    private List<CartItemDTO> cartItems;
    private Long totalPrice;

    public CartResponseDTO(Cart cart) {
    	this.id = cart.getId();
        this.cartItems = cart.getCartItems().stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }
}
