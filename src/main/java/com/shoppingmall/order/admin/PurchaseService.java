package com.shoppingmall.order.admin;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingmall.order.plus.PurchaseAll;

import jakarta.persistence.EntityNotFoundException;


@Service
public class PurchaseService {
	
	@Autowired
	PurchaseRepository repository;
	
	
	//주문
	public PurchaseAll orderList(PurchaseAll purchase, String reciveaddr) {
		purchase.setReciverAddr(purchase.getReciverAddr() + " " + reciveaddr);
		PurchaseAll savedPurchase = repository.save(purchase);
		return savedPurchase;
	}
	
	public PurchaseAll orderCancel(long id) {
	    PurchaseAll purchase = repository.findById(id)
	            .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
	        purchase.setCancelAt(LocalDateTime.now()); 
	        repository.save(purchase);
	        return purchase;
	}

    
    //전체 인원 목록 확인
    public Map<String, Object> orderAllList(String cancel) {
    	 List<PurchaseAll> purchases;
    	 String title;
    	 if (cancel.equals("cancel")) {
 	    	purchases = repository.findByCancelAtIsNotNull();
 	    	title = "취소 목록";
 	    }
 	    else if(cancel.equals("order")) {
 		    purchases = repository.findByCancelAtIsNull();
 		    title = "주문 목록";
 	    }
 	    else {
 	    	purchases = repository.findAll();
 	    	title = "주문/취소 전체목록";
 	    }
         Map<String, Object> response = new HashMap<>();
         response.put("title", title);
         response.put("purchases", purchases);
 	    return response;
    }
    
    //특정 id 기준 목록 확인
    public Map<String, Object> orderAllListByUserId(String cancel, String userId){
    	List<PurchaseAll> purchases;
    	String title;
   	 if (cancel.equals("cancel")) {
	    	purchases = repository.findByUserIdAndCancelAtIsNotNull(userId);
	    	title = "취소 목록";
	    }
	    else if(cancel.equals("order")) {
		    purchases = repository.findByUserIdAndCancelAtIsNull(userId);
		    title = "주문 목록";
	    }
	    else {
	    	purchases = repository.findByUserId(userId);
	    	title = "주문/취소 전체목록";
	    }
        Map<String, Object> response = new HashMap<>();
        response.put("title", title);
        response.put("purchases", purchases);
	    return response;
   }
}