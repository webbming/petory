package com.shoppingmall.cart.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.model.CartItem;
import com.shoppingmall.cart.model.CartResponseDTO;
import com.shoppingmall.cart.service.CartService;
import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
	
    private final CartService cartService;
    private final ProductService productService;

    // userId 추출
    private String getUserId(Authentication authentication) {
        return authentication.getName();
    }
    
    // 공통적인 오류 처리 메서드
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    
    // 장바구니 페이지
    @GetMapping("/")
    public String cartPage(Authentication authentication, Model model, HttpServletResponse response) {
    	
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        String userId = getUserId(authentication);
        Cart cart = cartService.getActiveCart(userId)
                .orElseGet(() -> cartService.createCart(userId));
        
        model.addAttribute("cart", cart);
        model.addAttribute("cartId", cart.getId());
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("cartMessage", cart.getCartItems().isEmpty() ? "장바구니가 비어 있습니다." : "");

        return "cart/cart"; 
    }

    // 상품 추가
    @PostMapping("/{id}/items/{productId}/add")
    public String addCartItem(Authentication authentication,
                              @PathVariable("id") Long id,
                              @PathVariable("productId") Long productId,
                              @RequestParam int quantity,
                              Model model) {
    	System.out.println("🛒 장바구니 담기 요청 - cartId: " + id + ", productId: " + productId + ", 수량: " + quantity);
        System.out.println("Cart ID: " + id);  // 확인용 로그
        System.out.println("Product ID: " + productId);  // 확인용 로그
        
        // 사용자 정보 확인
        String userId = getUserId(authentication);
        
        // 사용자가 가진 활성화된 장바구니 조회
        Cart cart = cartService.getActiveCart(userId).orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));
        System.out.println("활성화된 장바구니: " + cart.getId());  // 활성화된 장바구니 확인

        // 전달된 장바구니 ID가 사용자의 장바구니 ID와 일치하는지 확인
        if (!cart.getId().equals(id)) {
            model.addAttribute("error", "장바구니가 본인의 것이 아닙니다.");
            return "cart/cart";  // 에러 메시지를 모델에 담아서 다시 장바구니 페이지로 돌아감
        }
        
        try {
            Product product = productService.getProductById(productId);
            System.out.println("🛒 기존 장바구니 아이템 개수: " + cart.getCartItems().size());
            
            // 이미 장바구니에 있는 상품은 그대로 두고, 다른 상품을 추가
            cartService.addOrUpdateCartItem(userId, id, productId, quantity);  // 기존 상품에는 영향을 주지 않음

            // 장바구니 업데이트 후 모델에 추가
            cart = cartService.getActiveCart(userId).orElseThrow();
            
            model.addAttribute("cart", cart);
            model.addAttribute("cartId", cart.getId());
            model.addAttribute("cartItems", cart.getCartItems());
            
        } catch (IllegalArgumentException e) {
            return "redirect:/cart?error=" + e.getMessage();
        }

        return "cart/cart";  // 장바구니 페이지로 리다이렉트
    }

    @PutMapping("/{id}/items/{productId}/update")
    public ResponseEntity<?> updateCartItemQuantity(Authentication authentication,
                                                    @PathVariable Long id,
                                                    @PathVariable Long productId,
                                                    @RequestBody Map<String, Integer> requestBody) {
    	
    	String userId = getUserId(authentication);
        
    	// 수량이 1 이상인지 확인
        int quantity = requestBody.get("quantity");
        
        if (quantity <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "수량은 1 이상이어야 합니다."));
        }
        
        try {
            // 장바구니 아이템 추가/수정
            cartService.addOrUpdateCartItem(userId, id, productId, quantity);
           
            // 장바구니 업데이트 후 다시 가져오기
            Cart updatedCart = cartService.getActiveCart(userId)
                    .orElseThrow(() -> new IllegalArgumentException("장바구니를 찾을 수 없습니다."));

            // 총 금액 계산
            BigDecimal totalPrice = cartService.calculateTotalPrice(userId, id);

            // DTO 변환 후 반환
            CartResponseDTO cartResponseDTO = new CartResponseDTO(updatedCart);

            // 총 금액과 장바구니 정보를 반환
            return ResponseEntity.ok(Map.of("success", true, 
                                             "cart", cartResponseDTO,
                                             "totalPrice", totalPrice.toPlainString()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "잘못된 요청: " + e.getMessage()));
        }
    }


    // 장바구니 아이템 삭제(개별 삭제)
    @DeleteMapping("/{id}/items/{productId}/delete")
    public ResponseEntity<String> deleteCartItem(Authentication authentication,
                                                 @PathVariable Long id,
                                                 @PathVariable Long productId) {
        if (id == null || id <= 0 || productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        String userId = getUserId(authentication);
        try {
            cartService.removeCartItem(userId, id, productId);  // 장바구니 아이템 삭제 메서드 호출
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return handleIllegalArgumentException(e);
        }
    }
    
    // 장바구니 아이템 삭제(다중 삭제/ 전체 삭제랑 똑같음)
    @DeleteMapping("/{id}/items/delete-selected")
    public ResponseEntity<String> deleteSelectedCartItems(Authentication authentication, 
    													  @PathVariable Long id, 
    													  @RequestBody Map<String, List<Long>> requestData) { // 선택된 아이템 목록 받기
    	
        List<Long> selectedItems = requestData.get("selectedItems");
        System.out.println("Request data: " + requestData);
    	
    	if (selectedItems == null || selectedItems.isEmpty()) {
            return ResponseEntity.badRequest().body("선택된 상품이 없습니다.");
        }

        String userId = getUserId(authentication);
        try {
            cartService.removeSelectedCartItems(userId, id, selectedItems); // 선택된 아이템 삭제 메서드 호출
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
        	System.out.println("Error: " + e.getMessage());
            return handleIllegalArgumentException(e);
        }
    }
    
    // 선택된 상품 총 금액 조회
    @PostMapping("/{id}/selected-total-price")
    public ResponseEntity<Map<String, BigDecimal>> getSelectedItemsTotalPrice(
											    		Authentication authentication,
											    		@PathVariable Long id, 
											            @RequestBody Map<String, List<Long>> requestBody) {
    	
    	String userId = getUserId(authentication);
        List<Long> selectedItemIds = requestBody.get("selectedItemIds");
        
        System.out.println("📌 userId: " + userId);
        System.out.println("📌 cartId: " + id);
        System.out.println("📌 selectedItemIds: " + selectedItemIds);

        BigDecimal totalPrice = cartService.calculateSelectedItemsTotalPrice(userId, id, selectedItemIds);

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("totalPrice", totalPrice);
        return ResponseEntity.ok(response);
    }
    
    // 장바구니 총 금액 조회
    @GetMapping("/{id}/total-price")
    public ResponseEntity<?> getTotalPrice(Authentication authentication, @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 장바구니 ID입니다.");
        }

        String userId = getUserId(authentication);
        try {
            BigDecimal totalPrice = cartService.calculateTotalPrice(userId, id);
            return ResponseEntity.ok(totalPrice.toPlainString());
        } catch (IllegalArgumentException e) {
            return handleIllegalArgumentException(e);
        }
    }
}

