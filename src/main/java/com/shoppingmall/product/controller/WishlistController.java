package com.shoppingmall.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingmall.product.model.Wishlist;
import com.shoppingmall.product.service.WishlistService;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<Wishlist> addWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        Wishlist wishlist = wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeWishlist(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlistsByUser(@RequestParam Long userId) {
        List<Wishlist> wishlists = wishlistService.getWishlistsByUserId(userId);
        return ResponseEntity.ok(wishlists);
    }
}