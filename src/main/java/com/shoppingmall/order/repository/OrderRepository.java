package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    // Purchase 관련 쿼리 메소드
}

