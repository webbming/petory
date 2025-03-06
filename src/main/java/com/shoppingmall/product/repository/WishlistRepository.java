package com.shoppingmall.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.product.model.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Long userId);  // 유저의 찜 목록 조회
    Optional<Wishlist> findByUserIdAndProductProductId(Long userId, Long productId);  // ✅ 특정 유저의 찜 여부 확인
    void deleteByUserIdAndProductProductId(Long userId, Long productId);
}



