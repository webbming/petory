package com.shoppingmall.order.repository;


import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
  @Query("SELECT i FROM PurchaseItem i WHERE i.purchaseId.purchaseId = :purchaseId")
  List<PurchaseItem> findByPurchaseId(@Param("purchaseId") Long purchaseId);
}

