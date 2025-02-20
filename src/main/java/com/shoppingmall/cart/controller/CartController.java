package com.shoppingmall.cart.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    // 사용자별 장바구니 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Cart>> getCartList(@PathVariable Long userId) {
        List<Cart> cartList = cartService.getCartList(userId);
        return ResponseEntity.ok(cartList);
    }

    // 장바구니 화면 반환
    @GetMapping
    public String showCart() {
        return "cart";
    }

    // 장바구니 삭제
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long userId, @PathVariable Long cartId) {
        cartService.deleteCart(userId, cartId);
        return ResponseEntity.noContent().build();
    }

    // 장바구니에 상품 추가 또는 수량 업데이트
    @PostMapping("/{cartId}/items/{productId}")
    public ResponseEntity<String> addOrUpdateCartItem(@PathVariable Long userId, 
                                                      @PathVariable Long cartId, 
                                                      @PathVariable Long productId, 
                                                      @RequestParam int quantity) {
        cartService.addOrUpdateCartItem(userId, cartId, productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 장바구니에 추가되었습니다.");
    }

    // 장바구니 총 금액 계산
    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<BigDecimal> getTotalPrice(@PathVariable Long userId, @PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(userId, cartId);
        return ResponseEntity.ok(totalPrice);
    }
}
