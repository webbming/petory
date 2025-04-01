package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.PurchaseReturns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseReturnsRepository extends JpaRepository<PurchaseReturns, Long> {
    Page<PurchaseReturns> findAllByOrderByPurchaseReturnsIdDesc(Pageable pageable);
}
