package com.shoppingmall.order.plus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.order.plus.PurchaseAll;


@Repository
public interface PurchaseAllRepository extends JpaRepository<Purchase, Long> {
    // Purchase 관련 쿼리 메소드
}

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    // PurchaseItem 관련 쿼리 메소드
}

@Repository
public interface DeliveryRepository extends JpaRepository<PurchaseDelivery, Long> {
    // Delivery 관련 쿼리 메소드
}

