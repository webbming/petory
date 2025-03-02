package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    // Purchase 관련 쿼리 메소드
    List<Purchase> findByPurchaseId(Long purchaseId);

    @Query("SELECT p FROM Purchase p ORDER BY p.purchaseId DESC")
    Page<Purchase> findAllOrderByPurchaseIdDesc(Pageable pageable);

//    List<Purchase> findByUserIdOrderByPurchaseIdDesc(String userId);
//
//    List<Purchase> findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(String userId);
//
//    List<Purchase> findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(String userId);
//
//    List<Purchase> findByCancelAtIsNotNullOrderByPurchaseIdDesc();
//
//    List<Purchase> findByCancelAtIsNullOrderByPurchaseIdDesc();


    // userId로 주문 내역을 내림차순으로 찾기 (페이징)
    Page<Purchase> findByUserIdOrderByPurchaseIdDesc(String userId, Pageable pageable);

    // 취소된 주문을 userId로 내림차순으로 찾기 (페이징)
    Page<Purchase> findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(String userId, Pageable pageable);

    // 취소되지 않은 주문을 userId로 내림차순으로 찾기 (페이징)
    Page<Purchase> findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(String userId, Pageable pageable);

    // 취소된 주문을 내림차순으로 찾기 (페이징)
    Page<Purchase> findByCancelAtIsNotNullOrderByPurchaseIdDesc(Pageable pageable);

    // 취소되지 않은 주문을 내림차순으로 찾기 (페이징)
    Page<Purchase> findByCancelAtIsNullOrderByPurchaseIdDesc(Pageable pageable);

}

