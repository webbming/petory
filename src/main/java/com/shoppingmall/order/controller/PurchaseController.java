package com.shoppingmall.order.controller;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.dto.DeliveryChangeDto;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.dto.PurchasePageDto;
import com.shoppingmall.order.service.PurchaseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/order")
@Controller
public class PurchaseController {

@GetMapping
public String index() {
	return "order/index";
}

@GetMapping("/purchase")
public String purchase(){ return "order/purchaseReady";}

@GetMapping("/review")
public String review(){return "order/review";}

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
public String orderByPurchaseId(@PathVariable Long purchaseId,
																@RequestParam(name="admin", required = false, defaultValue = "user") String admin, Model model){
	PurchaseDto purchaseDto = service.getOrderDetails(purchaseId);
	model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseDto.getPurchase());
	model.addAttribute("item", purchaseDto.getPurchaseProduct());
	if(admin.equals("admin")){
		return "order/adminOrderResult";
	}
	return "order/orderResult";
}

//전체 회원 리스트 주문 검색(전체별, 취소별, 주문요청별)
@GetMapping("/admin/orderList")
public String orderAll(@RequestParam(name = "purchaseState", required = false, defaultValue = "all") String purchaseState,
											 @RequestParam(name = "page", defaultValue = "0") int page,
											 @RequestParam(name = "size", defaultValue = "3") int size,
											 Model model){
	Pageable pageable = PageRequest.of(page, size);
		model.addAttribute("purchase", service.purchaseList(purchaseState, pageable).getPurchase());
		model.addAttribute("delivery", service.purchaseList(purchaseState, pageable).getPurchaseDelivery());
		model.addAttribute("item", service.purchaseList(purchaseState, pageable).getPurchaseProduct());
	return "headerFragment/order/mypage-admin-purchaseAndDelivery";
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
	public String orderListByUserId(
			Authentication authentication,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size,
			@RequestParam(name = "purchaseState", required = false, defaultValue = "all") String purchaseState,
			@RequestParam(name = "admin", required = false, defaultValue = "user") String admin,
			Model model) {

		Pageable pageable = PageRequest.of(page, size);
		String userId = authentication.getName();

		// mushroom19 관리자의 경우
		if (userId.equals("mushroom19")) {
			PurchasePageDto adminPurchasePageDto = service.purchaseList(purchaseState, pageable);

			model.addAttribute("purchase", adminPurchasePageDto.getPurchase().getContent());
			model.addAttribute("delivery", adminPurchasePageDto.getPurchaseDelivery().getContent());
			model.addAttribute("item", adminPurchasePageDto.getPurchaseProduct().getContent());
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", adminPurchasePageDto.getPurchase().getTotalPages());

			return "headerFragment/order/mypage-admin-purchaseAndDelivery";
		}

		// 일반 유저의 경우
		PurchasePageDto purchasePageDto = service.orderListByUserId(userId, purchaseState, pageable);

		model.addAttribute("purchase", purchasePageDto.getPurchase().getContent());
		model.addAttribute("delivery", purchasePageDto.getPurchaseDelivery().getContent());
		model.addAttribute("item", purchasePageDto.getPurchaseProduct().getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", purchasePageDto.getPurchase().getTotalPages());

		if (admin.equals("admin")) {
			return "order/adminOrderByUserId";
		}
		return "headerFragment/order/mypage-common-purchaseAndDelivery";
	}




	//수령인 정보 변경
	@GetMapping("/orders/receiver")
	public String receiverChange(@ModelAttribute DeliveryChangeDto deliveryChangeDto,
								 @RequestParam(name="state", required = false, defaultValue = "show") String state,
								 Model model){
	model.addAttribute("delivery", service.receiverChange(deliveryChangeDto, state));
		if(state.equals("show")){
			model.addAttribute("purchaseId", deliveryChangeDto.getPurchaseId());
			return "order/receiverChange";
			}
	return "redirect:/order";
	}

}