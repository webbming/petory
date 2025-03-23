package com.shoppingmall.order.controller;

import com.shoppingmall.order.dto.CartToPuchaseDto;
import com.shoppingmall.order.dto.CouponListDto;
import com.shoppingmall.order.dto.DeliveryUpdateRequestDto;
import com.shoppingmall.order.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/order/rest")
@RestController
public class PurchaseRestController {

  final PurchaseService service;

    @PostMapping("/deliveryChange")
    public ResponseEntity<String> deliveryState(@RequestBody Map<String, String> request){
      String deliveryState = request.get("deliveryState");
      Long purchaseProductId = Long.parseLong(request.get("purchaseProductId"));
      service.deliveryChange(deliveryState, purchaseProductId);
      return ResponseEntity.ok("success");
    }

  //배송정보 수정
    @PostMapping("/receiverChange")
    public ResponseEntity<String> receiverChange(@ModelAttribute DeliveryUpdateRequestDto dto){
      return ResponseEntity.ok(service.change(dto));
    }

  //물품 구입
  @PostMapping("/process")
  public ResponseEntity<?> processOrder(@ModelAttribute CartToPuchaseDto cartToPuchaseDtos,
                                        Authentication authentication) {
    try {
      // 주문 처리 로직 진행
      String userId = authentication.getName();
      Long purchaseId = service.processOrder(cartToPuchaseDtos, userId);
      // 서비스 레이어로 주문 데이터 전달
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("purchaseId", purchaseId);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
    }
  }

  //소비자용 구매확정시 변환 로직
  @PostMapping("/purchaseConform")
  public ResponseEntity<Map<String, String>> purchaseConform(@RequestBody Map<String, String> request){
    service.purchaseConform(request.get("purchaseProductId"));
      return ResponseEntity.ok(Map.of("message", "구매 확정 완료"));
  }
}
