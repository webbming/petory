package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.PurchaseDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseDeliveryRepository extends JpaRepository<PurchaseDelivery, Long> {
  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchase.purchaseId = :purchaseId")
  List<PurchaseDelivery> findByPurchaseId(@Param("purchaseId") Long purchaseId);


  @Query("SELECT d FROM PurchaseDelivery d ORDER BY d.purchase.purchaseId DESC")
  Page<PurchaseDelivery> findAllOrderByPurchaseIdDesc(Pageable pageable);

  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchase.purchaseId = :purchaseId")
  PurchaseDelivery findPurchaseDeliveryByPurchaseId(Long purchaseId);

  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchase.userId = :userId ORDER BY d.purchase.purchaseId DESC")
  Page<PurchaseDelivery> findByUserIdOrderByPurchaseIdDesc(@Param("userId") String userId, Pageable pageable);

  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchase.cancelAt IS NOT NULL AND d.userId = :userId ORDER BY d.purchase.purchaseId DESC")
  Page<PurchaseDelivery> findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(String userId, Pageable pageable);

  @Query("SELECT d FROM PurchaseDelivery d WHERE d.purchase.cancelAt IS NULL AND d.userId = :userId ORDER BY d.purchase.purchaseId DESC")
  Page<PurchaseDelivery> findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(String userId, Pageable pageable);

  @Query("SELECT d FROM PurchaseDelivery d ORDER BY d.purchase.purchaseId DESC")
  Page<PurchaseDelivery> findByCancelAtIsNotNullOrderByPurchaseIdDesc(Pageable pageable);

  @Query("SELECT d FROM PurchaseDelivery d ORDER BY d.purchase.purchaseId DESC")
  Page<PurchaseDelivery> findByCancelAtIsNullOrderByPurchaseIdDesc(Pageable pageable);

}
