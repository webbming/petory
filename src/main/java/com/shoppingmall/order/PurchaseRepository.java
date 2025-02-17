package com.shoppingmall.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{
	
	List<Purchase> findByUserId(String userId);
	
	List<Purchase> findByCancelAtIsNull();
	
	List<Purchase> findByCancelAtIsNotNull();
	
	List<Purchase> findByIdAndCancelAtIsNotNull(String userId);
	
	List<Purchase> findByIdAndCancelAtIsNull(String userId);
}
