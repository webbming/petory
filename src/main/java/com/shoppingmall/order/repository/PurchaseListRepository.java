package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.PurchaseList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Long> {
    // Purchase 관련 쿼리 메소드
    List<PurchaseList> findByPurchaseId(Long purchaseId);

//    List<PurchaseList> findByPurchaseIdOrderByDesc();

}

