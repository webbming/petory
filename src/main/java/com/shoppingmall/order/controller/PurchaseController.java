package com.shoppingmall.order.controller;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.dto.DeliveryChangeDto;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@Controller
public class PurchaseController {

@GetMapping
public String index() {
	return "order/index";
}

@GetMapping("/purchase")
public String purchase(){ return "order/purchaseReady";}

@Autowired
PurchaseService service;

//주문 요청
@GetMapping("/order")
public String order(@ModelAttribute Purchase purchase, @ModelAttribute PurchaseDelivery delivery,
										@ModelAttribute PurchaseProduct item,
										@RequestParam(name = "receiver_addr_detail") String receiveDetailAddr,
										Model model){
	model.addAttribute("message", service.order(purchase, delivery, item, receiveDetailAddr));
	PurchaseDto purchaseDto = service.getOrderDetails(purchase.getPurchaseId());
	model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseDto.getPurchase());
	model.addAttribute("item", purchaseDto.getPurchaseProduct());
	return "order/orderResult";
}

	//주문번호 기준 주문검색
@GetMapping("/orders/{purchaseId}")
public String orderByPurchaseId(@PathVariable Long purchaseId, Model model){
	PurchaseDto purchaseDto = service.getOrderDetails(purchaseId);
	model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseDto.getPurchase());
	model.addAttribute("item", purchaseDto.getPurchaseProduct());
	return "order/orderResult";
}

//전체 회원 리스트 주문 검색(전체별, 취소별, 주문요청별)
@GetMapping("/admin/orderList")
public String orderAll(@RequestParam(name = "purchaseState", required = false, defaultValue = "all") String purchaseState, Model model){
		model.addAttribute("purchase", service.purchaseList(purchaseState).getPurchase());
		model.addAttribute("delivery", service.purchaseList(purchaseState).getPurchaseDelivery());
		model.addAttribute("item", service.purchaseList(purchaseState).getPurchaseProduct());
	return "order/orderResultAll";
	}

	//배송상태 변경
	@PostMapping("/admin/deiveryChange")
	public String deliveryChange(@RequestParam("deliveryState")String deliveryState,
								 @RequestParam("purchaseId")Long purchaseId,
										 Model model){
	model.addAttribute("message",service.deliveryChange(deliveryState, purchaseId));
	return "redirect:/order";
	}

	//주문 취소
	@PostMapping("/orders/{purchaseId}")
	public String orderCancel(@PathVariable Long purchaseId, Model model){
	model.addAttribute("message", service.purchaseCancel(purchaseId));
	return "redirect:/order";
	}

	//userId 기준 주문 검색
	@GetMapping("/orders/userId")
	public String orderListByUserId(@RequestParam(name = "userId") String userId
									,@RequestParam(name = "purchaseState", required = false) String purchaseState
									,@RequestParam(name = "admin", required = false, defaultValue = "user") String admin, Model model){
		if (purchaseState == null) {
			purchaseState = "all";
		}
	PurchaseDto purchaseDto = service.orderListByUserId(userId, purchaseState);
		model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
		model.addAttribute("purchase", purchaseDto.getPurchase());
		model.addAttribute("item", purchaseDto.getPurchaseProduct());
		if(admin.equals("admin")){
			return "order/adminOrderResult";
		}
	return "order/orderListByUserId";
	}
	
	//수령인 정보 변경
	@GetMapping("/orders/receiver")
	public String receiverChange(@ModelAttribute DeliveryChangeDto deliveryChangeDto,
								 @RequestParam(name="state", required = false, defaultValue = "show") String state,
								 Model model){
	System.out.println(state);
	System.out.println(deliveryChangeDto.getPurchaseId());
	model.addAttribute("delivery", service.receiverChange(deliveryChangeDto, state));
	Long purchaseId = deliveryChangeDto.getPurchaseId();
		if(state.equals("show")){
			model.addAttribute("purchaseId", deliveryChangeDto.getPurchaseId());
			return "order/receiverChange";
			}
	return "redirect:/order";
	}
}
