package com.shoppingmall.order.repository;


import com.shoppingmall.order.domain.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
  // PurchaseItem 관련 쿼리 메소드
}

