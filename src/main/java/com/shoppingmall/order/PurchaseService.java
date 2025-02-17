package com.shoppingmall.order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PurchaseService {
	
	@Autowired
	PurchaseRepository repository;
	
	
	//주문
	public Purchase orderList(Purchase purchase, String reciveaddr) {
		purchase.setReciverAddr(purchase.getReciverAddr() + " " + reciveaddr);
		Purchase savedPurchase = repository.save(purchase);
		return savedPurchase;
	}
	
    // 주문 취소
    public Optional<Purchase> orderCancel(int id) {
        Optional<Purchase> purchaseOptional = repository.findById(id);
        if (purchaseOptional.isPresent()) {
            Purchase purchase = purchaseOptional.get();
            purchase.setCancelAt(LocalDateTime.now()); // 취소 일자 추가
            repository.save(purchase); // 수정된 주문 저장
            return Optional.of(purchase); //값이 있을 경우 of 활용
        }
        // 주문이 없는 경우, 빈 Optional 반환
        return Optional.empty();
	}
    
    public Map<String, Object> orderList(String cancel) {
    	 List<Purchase> purchases;
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
    
    //특정 id 기준
    public Map<String, Object> orderAllListByUserId(String cancel, String userId){
    	List<Purchase> purchases;
    	String title;
   	 if (cancel.equals("cancel")) {
	    	purchases = repository.findByIdAndCancelAtIsNotNull(userId);
	    	title = "취소 목록";
	    }
	    else if(cancel.equals("order")) {
		    purchases = repository.findByIdAndCancelAtIsNull(userId);
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
}
