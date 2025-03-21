package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT d FROM Coupon d WHERE d.user.userId = :userId ORDER BY d.user.userId DESC")
    Page<Coupon> findByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT d FROM Coupon d WHERE d.user.userId = :userId ORDER BY d.user.userId DESC")
    List<Coupon> findByUserId(@Param("userId") String userId);
}
