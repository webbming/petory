package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.CouponList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponListRepository extends JpaRepository<CouponList, Long> {
}
