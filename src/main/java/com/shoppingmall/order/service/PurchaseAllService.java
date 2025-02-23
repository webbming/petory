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
	PurchaseDeliveryRepository delRepo;
	@Autowired
	PurchaseListRepository purRepo;
	@Autowired
	PurchaseItemRepository itemRepo;

	public String order(Purchase purchase, PurchaseDelivery delivery, PurchaseItem item, String receiveDetailAddr) {

		purchase.setCreateAt(LocalDateTime.now());
		item.setCreateAt(LocalDateTime.now());
		purRepo.save(purchase);
		itemRepo.save(item);
		delivery.setReceiverAddr(delivery.getReceiverAddr() + " " + receiveDetailAddr);
		delivery.setPurchaseId(purchase);
		delivery.setDeliveryStatus("배송준비중");
		delRepo.save(delivery);

		item.setPurchaseId(purchase);
		itemRepo.save(item);

		return "주문해주셔서 감사합니다";
	}

	public PurchaseAllDto allList(){
		List<Purchase> purchases = purRepo.findAll(Sort.by(Sort.Direction.DESC, "purchaseId"));
		List<PurchaseDelivery> deliveries = delRepo.findAllOrderByPurchaseIdDesc();
		List<PurchaseItem> items =itemRepo.findAllOrderByPurchaseIdDesc();

		return PurchaseAllDto.builder()
				.purchase(purchases)
				.purchaseDelivery(deliveries)
				.purchaseItem(items)
				.build();

	}
	
	public PurchaseAllDto getOrderDetails(Long purchaseId) {
		// Purchase 정보 가져오기
		List<Purchase> purchases = new ArrayList<>();
		List<PurchaseDelivery> deliveries = new ArrayList<>();
		List<PurchaseItem> items = new ArrayList<>();


//		if(purchaseId!=null){
		Purchase purchase = purRepo.findById(purchaseId)
				.orElseThrow(() -> new RuntimeException("Purchase not found for id: " + purchaseId));
		purchases = purRepo.findByPurchaseId(purchaseId);
		if (purchases.isEmpty()) {
			throw new RuntimeException("Purchases not found for id: " + purchaseId);
		}
		deliveries = delRepo.findByPurchaseId(purchaseId);  // 반환 타입은 List<PurchaseDelivery>
		if (deliveries.isEmpty()) {
			throw new RuntimeException("Delivery not found for id: " + purchaseId);
		}
		items = itemRepo.findByPurchaseId(purchaseId);
		if (items.isEmpty()) {
			throw new RuntimeException("Item not found for id: " + purchaseId);
		}
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//			String formattedDate = purchase.getCreateAt().format(formatter);
// 날짜 포맷팅
			return PurchaseAllDto.builder()
					.purchase(purchases)  // 전체 PurchaseList 리스트
					.purchaseDelivery(deliveries)  // 전체 PurchaseDelivery 리스트
					.purchaseItem(items)  // 전체 PurchaseItem 리스트
//					.formattedCreateAt(formattedDate)  // 포맷팅된 날짜
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

	public String deliveryChange(String deliveryState, Long purchaseId){
		 PurchaseDelivery changeState = delRepo.findPurchaseDeliveryByPurchaseId(purchaseId);
		System.out.println(deliveryState);
		System.out.println(purchaseId);
		if(deliveryState.equals("onDelivery")){
			deliveryState = "배송중";
		}
		else if(deliveryState.equals("onDelivery")){
			deliveryState = "배송완료";
		}
		else{
			deliveryState = "배송취소";
		}
			changeState.setDeliveryStatus(deliveryState);
			delRepo.save(changeState);
		return "주문상태 변경: " + deliveryState;
	}
}