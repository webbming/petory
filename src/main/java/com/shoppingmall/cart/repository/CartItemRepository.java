package com.shoppingmall.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	// 특정 장바구니 안에 있는 모든 아이템 조회
	List<CartItem> findByCartCartId(Long cartId);
	
	// 특정 상품이 장바구니에 있는지 조회
	Optional<CartItem> findByCartCartIdAndProduct_ProductId(Long cartId, Long productId);
	
	// 특정 상품 삭제
	void deleteByCartCartIdAndProduct_ProductId(Long cartId, Long productId);
	
}