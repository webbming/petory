package com.shoppingmall.order.repository;


import com.shoppingmall.order.domain.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
  @Query("SELECT i FROM PurchaseItem i WHERE i.purchase.purchaseId = :purchaseId")
  List<PurchaseItem> findByPurchaseId(@Param("purchaseId") Long purchaseId);

  @Query("SELECT d FROM PurchaseItem d ORDER BY d.purchase.purchaseId DESC")
  List<PurchaseItem> findAllOrderByPurchaseIdDesc();

  @Query("SELECT d FROM PurchaseItem d WHERE d.purchase.userId = :userId ORDER BY d.purchase.purchaseId DESC")
  List<PurchaseItem> findByUserIdOrderByPurchaseIdDesc(@Param("userId") String userId);

  @Query("SELECT d FROM PurchaseItem d WHERE d.purchase.cancelAt IS NOT NULL AND d.userId = :userId ORDER BY d.purchase.purchaseId DESC")
  List<PurchaseItem> findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(String userId);

  @Query("SELECT d FROM PurchaseItem d WHERE d.purchase.cancelAt IS NULL AND d.userId = :userId ORDER BY d.purchase.purchaseId DESC")
  List<PurchaseItem> findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(String userId);
}

