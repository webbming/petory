package com.shoppingmall.order.plus;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import com.shoppingmall.order.dto.PurchaseAllDto;
import com.shoppingmall.order.repository.PurchaseDeliveryRepository;
import com.shoppingmall.order.repository.PurchaseItemRepository;
import com.shoppingmall.order.repository.PurchaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PurchaseAllService {

	@Autowired
	PurchaseDeliveryRepository delRepo;
	@Autowired
	PurchaseListRepository purRepo;
	@Autowired
	PurchaseItemRepository itemRepo;

	public String order(PurchaseList purchase, PurchaseDelivery delivery, PurchaseItem item) {
		purRepo.save(purchase);

		delivery.setPurchaseId(purchase); // PurchaseDelivery에 PurchaseList 설정
		delivery.setDeliveryStatus("배송준비중");
		delRepo.save(delivery);

		item.setPurchaseId(purchase); // PurchaseItem에도 PurchaseList 설정
		itemRepo.save(item);

		return "주문해주셔서 감사합니다";
	}
	
	public List<PurchaseAllDto> getOrderDetails(Long purchaseId) {
		// Purchase 정보 가져오기
		PurchaseList purchase = purRepo.findById(purchaseId)
				.orElseThrow(() -> new RuntimeException("Purchase not found for id: " + purchaseId));

		// Delivery 정보 가져오기 (반환 타입 수정)
		List<PurchaseDelivery> deliveries = delRepo.findByPurchaseId(purchaseId);  // 반환 타입은 List<PurchaseDelivery>
		if (deliveries.isEmpty()) {
			throw new RuntimeException("Delivery not found for id: " + purchaseId);
		}
		PurchaseDelivery delivery = deliveries.get(0);  // 첫 번째 배송 정보 선택

		// PurchaseItem 정보 가져오기
		List<PurchaseItem> items = itemRepo.findByPurchaseId(purchaseId);
		if (items.isEmpty()) {
			throw new RuntimeException("Item not found for id: " + purchaseId);
		}

		// PurchaseAllDto 객체 생성하여 반환
		PurchaseAllDto purchaseAllDto = PurchaseAllDto.builder()
				.purchaseList(purchase)
				.purchaseDelivery(delivery)
				.purchaseItems(items)
				.build();

		// 하나의 DTO를 리스트에 담아서 반환
		List<PurchaseAllDto> dto = new ArrayList<>();
		dto.add(purchaseAllDto);

		return dto;
	}

}