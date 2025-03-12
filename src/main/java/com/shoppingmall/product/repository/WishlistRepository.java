package com.shoppingmall.product.repository;

import com.shoppingmall.product.model.Product;
import com.shoppingmall.user.model.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shoppingmall.product.model.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Query("SELECT w FROM Wishlist w where  w.user.id = :userId ORDER BY w.addedOn DESC")
    List<Wishlist> findTop5ByUserId(Long userId , PageRequest pageable);  // 유저의 찜 목록 조회
    Optional<Wishlist> findByUserIdAndProductProductId(Long userId, Long productId);  // ✅ 특정 유저의 찜 여부 확인
    void deleteByUserIdAndProductProductId(Long userId, Long productId);

    List<Product> findProductByUserId(Long id);

    Long user(User user);
}



