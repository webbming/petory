package com.shoppingmall.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	List<CartItem> findByCart_CartId(Long cartId); // 특정 장바구니에 있는 아이템 조회
    Optional<CartItem> findByCart_CartIdAndProduct_ProductId(Long cartId, Long productId); // 장바구니 내 상품 조회
    Optional<CartItem> findByCart_CartIdAndCart_User_UserId(Long cartId, String userId); // 장바구니 내 사용자 아이디로 아이템 조회
}