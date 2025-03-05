package com.shoppingmall.order.service;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.dto.DeliveryChangeDto;
import com.shoppingmall.order.dto.PurchaseDeliveryDto;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.repository.PurchaseDeliveryRepository;
import com.shoppingmall.order.repository.PurchaseProductRepository;
import com.shoppingmall.order.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class PurchaseService {

	@Autowired
	PurchaseDeliveryRepository deliveryRepo;
	@Autowired
	PurchaseRepository purchaseRepo;
	@Autowired
    PurchaseProductRepository productRepo;

	public String order(Purchase purchase, PurchaseDelivery delivery, PurchaseProduct item, String receiveDetailAddr) {

		purchase.setCreateAt(LocalDateTime.now());
		item.setCreateAt(LocalDateTime.now());
		delivery.setUserId(purchase.getUserId());
		item.setUserId(purchase.getUserId());
		purchaseRepo.save(purchase);
		productRepo.save(item);
		delivery.setReceiverAddr(delivery.getReceiverAddr() + " " + receiveDetailAddr);
		delivery.setPurchase(purchase);
		delivery.setDeliveryStatus("배송준비중");
		deliveryRepo.save(delivery);

		item.setPurchase(purchase);
		productRepo.save(item);

		return "주문해주셔서 감사합니다";
	}

	//전체 주문 / 취소 검색
	public PurchaseDto purchaseList(String purchasestate){
		List<Purchase> purchases;
		List<PurchaseDelivery> deliveries;
		List<PurchaseProduct> products;

		if(purchasestate.equals("all")){
			purchases = purchaseRepo.findAll(Sort.by(Sort.Direction.DESC, "purchaseId"));
			deliveries = deliveryRepo.findAllOrderByPurchaseIdDesc();
			products = productRepo.findAllOrderByPurchaseIdDesc();
		}
		else if(purchasestate.equals("cancel")){
			purchases = purchaseRepo.findByCancelAtIsNotNullOrderByPurchaseIdDesc();
			deliveries = deliveryRepo.findByCancelAtIsNotNullOrderByPurchaseIdDesc();
			products = productRepo.findByCancelAtIsNotNullOrderByPurchaseIdDesc();
		}
		else{
			purchases = purchaseRepo.findByCancelAtIsNullOrderByPurchaseIdDesc();
			deliveries = deliveryRepo.findByCancelAtIsNullOrderByPurchaseIdDesc();
			products = productRepo.findByCancelAtIsNullOrderByPurchaseIdDesc();
		}

		return PurchaseDto.builder()
				.purchase(purchases)
				.purchaseDelivery(deliveries)
				.purchaseProduct(products)
				.build();

	}

	//주문번호로 상세 주문 검색
	public PurchaseDto getOrderDetails(Long purchaseId) {

		List<Purchase> purchases = purchaseRepo.findByPurchaseId(purchaseId);
		List<PurchaseDelivery> deliveries  = deliveryRepo.findByPurchaseId(purchaseId);
		List<PurchaseProduct> products = productRepo.findByPurchaseId(purchaseId);

		return PurchaseDto.builder()
				.purchase(purchases)
				.purchaseDelivery(deliveries)
				.purchaseProduct(products)
				.build();
	}

	//userId별 주문 검색
	public PurchaseDto orderListByUserId(String userId, String purchaseState){
		List<Purchase> purchases;
		List<PurchaseDelivery> deliveries;
		List<PurchaseProduct> products;

		if(purchaseState.equals("all")){
			purchases =  purchaseRepo.findByUserIdOrderByPurchaseIdDesc(userId);
			deliveries = deliveryRepo.findByUserIdOrderByPurchaseIdDesc(userId);
			products = productRepo.findByUserIdOrderByPurchaseIdDesc(userId);
		}
		else if(purchaseState.equals("cancel")){
			purchases = purchaseRepo.findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(userId);
			deliveries = deliveryRepo.findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(userId);
			products = productRepo.findByCancelAtIsNotNullAndUserIdOrderByPurchaseIdDesc(userId);
		}
		else{
			purchases = purchaseRepo.findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(userId);
			deliveries = deliveryRepo.findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(userId);
			products = productRepo.findByCancelAtIsNullAndUserIdOrderByPurchaseIdDesc(userId);
		}
		return PurchaseDto.builder()
				.purchase(purchases)
				.purchaseDelivery(deliveries)
				.purchaseProduct(products)
				.build();
	}

	//배송 상태 변경
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
	
//주문 취소
	public String purchaseCancel(Long purchaseId){
		List<Purchase> purchases = purchaseRepo.findByPurchaseId(purchaseId);
		List<PurchaseDelivery> deliveries = deliveryRepo.findByPurchaseId(purchaseId);
		List<PurchaseProduct> products = productRepo.findByPurchaseId(purchaseId);
		Purchase purchase = purchases.get(0);
		PurchaseDelivery delivery = deliveries.get(0);
		PurchaseProduct product = products.get(0);

		product.setCancelAt(LocalDateTime.now());
		delivery.setCancelAt(LocalDateTime.now());
		purchase.setCancelAt(LocalDateTime.now());

		deliveryRepo.save(delivery);
		productRepo.save(product);
		purchaseRepo.save(purchase);
		return "취소되었습니다";
	}
	
	//배송정보 수정 관련
	public PurchaseDeliveryDto receiverChange(DeliveryChangeDto dto, String state){
		System.out.print(dto.getPurchaseId());
	 	List<PurchaseDelivery> receiver = deliveryRepo.findByPurchaseId(dto.getPurchaseId());
		 PurchaseDelivery receiverChanse = receiver.get(0);
		 if(state.equals("change")){
			 receiverChanse.setReceiverName(dto.getReceiverName());
			 receiverChanse.setReceiverPhone(dto.getReceiverPhone());
				 if(dto.getDetailAddr()==null){
					 dto.setDetailAddr("");
				 }
			 receiverChanse.setReceiverAddr(dto.getReceiverAddr() + "  " + dto.getDetailAddr());
			 receiverChanse.setDeliveryMessage(dto.getDeliveryMessage());
			 deliveryRepo.save(receiverChanse);
		 }
		return PurchaseDeliveryDto.builder()
				.deliveryId(receiverChanse.getDeliveryId())
				.receiverName(receiverChanse.getReceiverName())
				.receiverAddr(receiverChanse.getReceiverAddr())
				.receiverPhone(receiverChanse.getReceiverPhone())
				.deliveryMessage(receiverChanse.getDeliveryMessage())
				.build();
	}
}