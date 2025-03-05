package com.shoppingmall.order.controller;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseProduct;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseReview;
import com.shoppingmall.order.dto.DeliveryChangeDto;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.dto.PurchasePageDto;
import com.shoppingmall.order.repository.PurchaseReviewRepository;
import com.shoppingmall.order.service.PurchaseService;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

@Autowired
PurchaseReviewRepository purchaseReviewRepository;

@PostMapping("/purchase/review")
public String reviews(@RequestParam(name = "productId")Long productId,
											@RequestParam(name = "comment") String comment,
											@RequestParam(name = "rating") int rating,
											@RequestParam(name = "reviewImages") List<MultipartFile> reviewImages,
											Model model){
	System.out.println(productId);
	System.out.println(comment);
	System.out.println(reviewImages);
	List<String> imagePaths = new ArrayList<>();

	try {
		for (MultipartFile file : reviewImages) {
			String fileName = file.getOriginalFilename();
			String filePath = "/images/" + fileName;  // 이미지 경로

			// 실제 파일 저장은 하지 않지만, 경로는 DB에 저장
			imagePaths.add(filePath);
		}

		// Review 객체 생성
		PurchaseReview review = new PurchaseReview();
		review.setComment(comment);
		review.setImagePaths(imagePaths);
		review.setRating(rating);

		// 리뷰 저장 (DB에 저장)
		purchaseReviewRepository.save(review);

		// 모델에 경로 넘기기
		model.addAttribute("imagePaths", imagePaths);
	} catch (IOException e) {
		e.printStackTrace();
	}

	return "order/review-page";  // 리뷰 페이지로 리턴
}

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
	PurchasePageDto purchasePageDto = service.purchaseList(purchaseState, pageable);
	// 페이지 정보를 모델에 추가
	model.addAttribute("purchase", purchasePageDto.getPurchase());
	model.addAttribute("delivery", purchasePageDto.getPurchaseDelivery());
	model.addAttribute("item", purchasePageDto.getPurchaseProduct());
	model.addAttribute("currentPage", page); // 현재 페이지
	model.addAttribute("totalPages", purchasePageDto.getPurchase().getTotalPages()); // 전체 페이지 수
	model.addAttribute("pageSize", size);
return "order/orderResultAll";
//	return "headerFragment/order/mypage-admin-purchaseAndDelivery";
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
									@RequestParam(name = "userId", required = false, defaultValue = "null") String userId,
									Model model) {
		Pageable pageable = PageRequest.of(page, size);

		// mushroom19 관리자의 경우
		if (admin.equals("admin")) {
			PurchasePageDto adminPurchasePageDto = service.orderListByUserId(userId, purchaseState, pageable);

			model.addAttribute("purchase", adminPurchasePageDto.getPurchase());
			model.addAttribute("delivery", adminPurchasePageDto.getPurchaseDelivery());
			model.addAttribute("item", adminPurchasePageDto.getPurchaseProduct());
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", adminPurchasePageDto.getPurchase().getTotalPages());
			model.addAttribute("pageSize", size);


//			return "headerFragment/order/mypage-admin-purchaseAndDelivery";
			return "order/adminOrderByUserId";
		}

		// 일반 유저의 경우
		PurchasePageDto purchasePageDto = service.orderListByUserId(userId, purchaseState, pageable);
		model.addAttribute("aa", purchasePageDto);
		model.addAttribute("purchase", purchasePageDto.getPurchase());
		model.addAttribute("delivery", purchasePageDto.getPurchaseDelivery());
		model.addAttribute("item", purchasePageDto.getPurchaseProduct());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", purchasePageDto.getPurchase().getTotalPages());
		model.addAttribute("pageSize", size);
//		return "headerFragment/order/mypage-common-purchaseAndDelivery";
//		return "order/orderListByUserId";
		return "order/orderListByUserIdByProductId";
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