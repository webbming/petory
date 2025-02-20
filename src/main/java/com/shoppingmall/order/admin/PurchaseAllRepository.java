package com.shoppingmall.order.admin;

import com.shoppingmall.order.plus.PurchaseAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseAllRepository extends JpaRepository<PurchaseAll, Long> {
    // Purchase 관련 쿼리 메소드
}

