package com.shoppingmall.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.product.model.Wishlist;
import com.shoppingmall.product.service.WishlistService;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // 찜한 상품 저장
    @PostMapping("/add")
    public ResponseEntity<?> addProductToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            Wishlist added = wishlistService.addProductToWishlist(userId, productId);
            return ResponseEntity.ok(added);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product to wishlist: " + e.getMessage());
        }
    }



    @DeleteMapping("/remove")
    public ResponseEntity<?> removeProductFromWishlist(
            @RequestParam Long userId, 
            @RequestParam Long productId) {

        if (userId == null || productId == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
        }

        wishlistService.removeProductFromWishlist(userId, productId);
        return ResponseEntity.ok("찜 목록에서 상품이 삭제되었습니다.");
    }
    
 // ✅ 로그인 없이 모든 찜 목록 조회 가능하도록 변경
    @GetMapping
    public String showWishlist(Model model) {
        List<Wishlist> wishlists = wishlistService.getAllWishlists();
        model.addAttribute("wishlistItems", wishlists);
        return "wishlist/wishlist";
    }    


}
