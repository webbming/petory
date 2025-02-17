package com.shoppingmall.order;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PurchaseService {
	
	@Autowired
	PurchaseRepository repository;
	
    // 주문 취소 메서드
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
	
}
