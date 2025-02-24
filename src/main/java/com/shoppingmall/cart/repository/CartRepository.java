package com.shoppingmall.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	// 특정 사용자의 모든 장바구니 조회
    List<Cart> findByUser_UserId(String userId);
    
    // 특정 사용자의 활성화된 장바구니 조회
    List<Cart> findByUser_UserIdAndIsActiveTrue(String userId);
    
    // 특정 사용자의 특정 장바구니 조회
    Optional<Cart> findByCartIdAndUser_UserId(Long cartId, String userId);

}
