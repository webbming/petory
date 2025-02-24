package com.shoppingmall.order.service;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.dto.PurchaseAllDto;
import com.shoppingmall.order.dto.PurchaseItemDto;
import com.shoppingmall.order.repository.PurchaseDeliveryRepository;
import com.shoppingmall.order.repository.PurchaseItemRepository;
import com.shoppingmall.order.repository.PurchaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PurchaseAllService {

	@Autowired
	PurchaseDeliveryRepository deliveryRepo;
	@Autowired
	PurchaseListRepository purchaseRepo;
	@Autowired
	PurchaseItemRepository itemRepo;

	public String order(Purchase purchase, PurchaseDelivery delivery, PurchaseItem item, String receiveDetailAddr) {

		purchase.setCreateAt(LocalDateTime.now());
		item.setCreateAt(LocalDateTime.now());
		delivery.setUserId(purchase.getUserId());
		item.setUserId(purchase.getUserId());
		purchaseRepo.save(purchase);
		itemRepo.save(item);
		delivery.setReceiverAddr(delivery.getReceiverAddr() + " " + receiveDetailAddr);
		delivery.setPurchase(purchase);
		delivery.setDeliveryStatus("배송준비중");
		deliveryRepo.save(delivery);

		item.setPurchase(purchase);
		itemRepo.save(item);

		return "주문해주셔서 감사합니다";
	}

	//전체 주문 /취소 검색
	public PurchaseAllDto allList(){
		List<Purchase> purchases = purchaseRepo.findAll(Sort.by(Sort.Direction.DESC, "purchaseId"));
		List<PurchaseDelivery> deliveries = deliveryRepo.findAllOrderByPurchaseIdDesc();
		List<PurchaseItem> items =itemRepo.findAllOrderByPurchaseIdDesc();

//		List<String> purchaseIdList = purRepo.findIdByUserId();
		return PurchaseAllDto.builder()
				.purchase(purchases)
				.purchaseDelivery(deliveries)
				.purchaseItem(items)
				.build();

	}

	//주문번호 기준 주문기록 검색
	public PurchaseAllDto getOrderDetails(Long purchaseId) {

		Purchase purchase = purchaseRepo.findById(purchaseId)
				.orElseThrow(() -> new RuntimeException("Purchase not found for id: " + purchaseId));
		List<Purchase> purchases = purchaseRepo.findByPurchaseId(purchaseId);
		if (purchases.isEmpty()) {
			throw new RuntimeException("Purchases not found for id: " + purchaseId);
		}
		List<PurchaseDelivery> deliveries  = deliveryRepo.findByPurchaseId(purchaseId);  // 반환 타입은 List<PurchaseDelivery>
		if (deliveries.isEmpty()) {
			throw new RuntimeException("Delivery not found for id: " + purchaseId);
		}
		List<PurchaseItem> items = itemRepo.findByPurchaseId(purchaseId);
		if (items.isEmpty()) {
			throw new RuntimeException("Item not found for id: " + purchaseId);
		}
			return PurchaseAllDto.builder()
					.purchase(purchases)  // 전체 PurchaseList 리스트
					.purchaseDelivery(deliveries)  // 전체 PurchaseDelivery 리스트
					.purchaseItem(items)  // 전체 PurchaseItem 리스트
					.build();
	}

		public PurchaseItemDto orderDetail(Long purchaseItemId){
			Optional<PurchaseItem> purchaseItem = itemRepo.findById(purchaseItemId);

			PurchaseItem item = purchaseItem.get();

			return PurchaseItemDto.builder()
					.orderItemId(item.getPurchaseItemId())
					.productId(item.getProductId())
					.productName(item.getProductName())
					.option(item.getOption())
					.quantity(item.getQuantity())
					.price(item.getPrice())
					.totalPrice(item.getTotalPrice())
					.createAt(item.getCreateAt())
					.cancelAt(item.getCancelAt())
					.build();
	}

	public PurchaseAllDto orderListByUserId(String userId){
		List<Purchase> purchases =  purchaseRepo.findByUserIdOrderByPurchaseIdDesc(userId);
		List<PurchaseDelivery> deliveries = deliveryRepo.findByUserIdOrderByPurchaseIdDesc(userId);
		List<PurchaseItem> items =itemRepo.findByUserIdOrderByPurchaseIdDesc(userId);

//		List<String> purchaseIdList = purRepo.findIdByUserId();
		return PurchaseAllDto.builder()
				.purchase(purchases)
				.purchaseDelivery(deliveries)
				.purchaseItem(items)
				.build();
	}


	public String deliveryChange(String deliveryState, Long purchaseId){
		 PurchaseDelivery changeState = deliveryRepo.findPurchaseDeliveryByPurchaseId(purchaseId);
		if(deliveryState.equals("onDelivery")){
			deliveryState = "배송중";
		}
		else if(deliveryState.equals("finDelivery")){
			deliveryState = "배송완료";
		}
		else{
			deliveryState = "배송취소";
		}
			changeState.setDeliveryStatus(deliveryState);
			deliveryRepo.save(changeState);
		return "주문상태 변경: " + deliveryState;
	}

	public String orderCancel(Long purchaseId){
		List<Purchase> purchases = purchaseRepo.findByPurchaseId(purchaseId);
		Purchase purchase = purchases.get(0);
		purchase.setCancelAt(LocalDateTime.now());
		return "취소되었습니다";
	}
}