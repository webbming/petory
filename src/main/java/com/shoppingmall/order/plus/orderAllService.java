package com.shoppingmall.order.plus;

import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.dto.OrderListDto;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.repository.DeliveryRepository;
import com.shoppingmall.order.repository.OrderItemRepository;
import com.shoppingmall.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PurchaseAllService {

	@Autowired
	DeliveryRepository delRepo;
	@Autowired
	OrderRepository purRepo;
	@Autowired
	OrderItemRepository itemRepo;





	public String order(Purchase purchase, PurchaseDelivery delivery, PurchaseItem item) {
			purRepo.save(purchase);
			delRepo.save(delivery);
			itemRepo.save(item);

		getOrderDetails(purchase.getOrderId());


			return "회원가입을 축하드립니다.";

	}

	public OrderListDto getOrderDetails(Long orderId) {
		// Purchase 정보 가져오기
		Purchase purchase = purRepo.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found"));

		// Delivery 정보 가져오기
		PurchaseDelivery delivery = delRepo.findByPurchase(purchase)
				.orElseThrow(() -> new RuntimeException("Delivery not found"));

		// PurchaseItem 정보 가져오기
		List<PurchaseItem> items = itemRepo.findByPurchase(purchase);

		// OrderListDto로 변환
		if (!items.isEmpty()) {
			return new OrderListDto(purchase, delivery, items.get(0)); // 예시로 첫 번째 상품 사용
		} else {
			throw new RuntimeException("No items found for this order");
		}
	}

	}