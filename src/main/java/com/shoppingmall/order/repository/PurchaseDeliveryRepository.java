package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.PurchaseDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDeliveryRepository extends JpaRepository<PurchaseDelivery, Long> {
  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchaseId.purchaseId = :purchaseId")
  List<PurchaseDelivery> findByPurchaseId(@Param("purchaseId") Long purchaseId);


  @Query("SELECT d FROM PurchaseDelivery d ORDER BY d.purchaseId.purchaseId DESC")
  List<PurchaseDelivery> findAllOrderByPurchaseIdDesc();

  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchaseId.purchaseId = :purchaseId")
  PurchaseDelivery findPurchaseDeliveryByPurchaseId(Long purchaseId);

}
