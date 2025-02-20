package com.shoppingmall.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findById(Long cartId); // Optional<Cart> 반환
	
	// 유저 아이디로 장바구니 조회
	List<Cart> findByUserUserId(Long userId);

}
