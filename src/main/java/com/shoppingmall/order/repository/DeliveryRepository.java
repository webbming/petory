package com.shoppingmall.order.repository;

import com.shoppingmall.order.plus.PurchaseDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<PurchaseDelivery, Long> {
  // Delivery 관련 쿼리 메소드
}
