package com.shoppingmall.order.admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.order.plus.PurchaseAll;


@Repository
public interface OrderRepository extends JpaRepository<PurchaseAll, Long>{
	
	List<PurchaseAll> findByUserId(String userId);
	
	List<PurchaseAll> findByCancelAtIsNull();
	
	List<PurchaseAll> findByCancelAtIsNotNull();
	
	List<PurchaseAll> findByUserIdAndCancelAtIsNotNull(String userId);
	
	List<PurchaseAll> findByUserIdAndCancelAtIsNull(String userId);
}
