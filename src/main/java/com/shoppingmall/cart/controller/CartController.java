package com.shoppingmall.cart.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    
    // 장바구니 화면 반환
    @GetMapping("/show")
    public String cartPage() {
        return "/cart/cart";
    }


    // 사용자별 장바구니 조회
    @GetMapping("/carts")
    public ResponseEntity<List<Cart>> getCartList(Authentication authentication) {
    	String userId = (String) authentication.getName();
        List<Cart> cartList = cartService.getCartList(userId);
        if(cartList.isEmpty()) {
        	return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cartList);
    }

  
    // 장바구니 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(Authentication authentication, @PathVariable Long cartId) {
    	String userId = (String) authentication.getName();
        cartService.deleteCart(userId, cartId);
        return ResponseEntity.noContent().build();
    }

    // 상품 추가
    @PostMapping("/{cartId}/items/{productId}/add")
    public ResponseEntity<String> addCartItem(Authentication authentication, 
                                               @PathVariable Long cartId, 
                                               @PathVariable Long productId, 
                                               @RequestParam int quantity) {
        String userId = (String) authentication.getName();
        cartService.addCartItem(userId, cartId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 장바구니에 추가되었습니다.");
    }

    // 수량 업데이트
    @PutMapping("/{cartId}/items/{productId}/update")
    public ResponseEntity<String> updateCartItemQuantity(Authentication authentication, 
                                                         @PathVariable Long cartId, 
                                                         @PathVariable Long productId, 
                                                         @RequestParam int quantity) {
        String userId = (String) authentication.getName();
        cartService.updateCartItemQuantity(userId, cartId, productId, quantity);
        return ResponseEntity.ok("상품 수량이 업데이트되었습니다.");
    }

    // 장바구니 총 금액 조회 
    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<BigDecimal> getTotalPrice(Authentication authentication, @PathVariable Long cartId) {
    	String userId = (String) authentication.getName();
        BigDecimal totalPrice = cartService.getTotalPrice(userId, cartId);
        return ResponseEntity.ok(totalPrice);
    }
}
