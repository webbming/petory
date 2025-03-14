package com.shoppingmall.order.repository;

import com.shoppingmall.order.domain.PurchaseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseReviewRepository extends JpaRepository<PurchaseReview, Long> {
}
