package com.shoppingmall.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.model.CartItem;
import com.shoppingmall.product.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}