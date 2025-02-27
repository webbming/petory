package com.shoppingmall.cart.controller;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.service.CartService;
import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    
    // 장바구니 페이지
    @GetMapping("/cart")
    public String cartPage(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";  // 로그인 페이지로 리다이렉트
        }

        String userId = authentication.getName();
        Cart cart = cartService.getActiveCart(userId)
                .orElseGet(() -> cartService.createCart(userId));
        
        
        
        model.addAttribute("cart", cart);
        model.addAttribute("cartId", cart.getId());
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("cartMessage", cart.getCartItems().isEmpty() ? "장바구니가 비어 있습니다." : "");

        return "cart/cart"; // 장바구니 화면 반환
    }

    // 상품 추가
    @PostMapping("/{id}/items/{productId}/add")
    public String addCartItem(Authentication authentication,
                              @PathVariable Long id,
                              @PathVariable Long productId,
                              @RequestParam int quantity,
                              Model model) {
        if (id == null || id <= 0) {
            return "redirect:/cart?error=Invalid cartId";
        }

        String userId = authentication.getName();
        try {
            Product product = productService.getProductById(productId);
            cartService.addOrUpdateCartItem(userId, id, productId, quantity);
            // 데이터를 모델에 추가하여 화면에 전달
            Cart cart = cartService.getActiveCart(userId).orElseThrow();
            model.addAttribute("cart", cart);
            model.addAttribute("cartId", cart.getId());
            model.addAttribute("cartItems", cart.getCartItems());
        } catch (IllegalArgumentException e) {
            return "redirect:/cart?error=" + e.getMessage();
        }

        return "cart/cart"; // 장바구니 페이지로 리다이렉트하면서 모델 데이터 전달
    }

    // 수량 업데이트
    @PutMapping("/{id}/items/{productId}/update")
    public ResponseEntity<String> updateCartItemQuantity(Authentication authentication,
                                                         @PathVariable Long id,
                                                         @PathVariable Long productId,
                                                         @RequestParam int quantity) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 장바구니 ID입니다.");
        }
        
        if(quantity <= 0) {
        	return ResponseEntity.badRequest().body("수량이 1 이상이어야 합니다.");
        }

        String userId = authentication.getName();
        try {
        	// 수량 업데이트
            cartService.addOrUpdateCartItem(userId, id, productId, quantity);
            return ResponseEntity.ok("상품 수량이 업데이트되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 장바구니 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(Authentication authentication, @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 장바구니 ID입니다.");
        }

        String userId = authentication.getName();
        try {
            cartService.removeCart(userId, id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 장바구니 총 금액 조회
    @GetMapping("/{id}/total-price")
    public ResponseEntity<?> getTotalPrice(Authentication authentication, @PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("유효하지 않은 장바구니 ID입니다.");
        }

        String userId = authentication.getName();
        try {
            BigDecimal totalPrice = cartService.calculateTotalPrice(userId, id);
            return ResponseEntity.ok(totalPrice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

