package com.shoppingmall.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.product.model.Wishlist;
import com.shoppingmall.product.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProductToWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            Wishlist added = wishlistService.addProductToWishlist(userId, productId);
            return ResponseEntity.ok(added);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product to wishlist: " + e.getMessage());
        }
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeProductFromWishlist(@PathVariable Long id) {
        wishlistService.removeProductFromWishlist(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Wishlist>> getUserWishlist(@PathVariable Long userId) {
        List<Wishlist> wishlists = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity.ok(wishlists);
    }
    
    @GetMapping("/wishlist")
    public String showWishlist(Model model, @RequestParam Long userId) {
        List<Wishlist> wishlists = wishlistService.getWishlistByUserId(userId);
        model.addAttribute("wishlistItems", wishlists);
        return "wishlist"; // Thymeleaf 템플릿 이름을 반환
    }


}
