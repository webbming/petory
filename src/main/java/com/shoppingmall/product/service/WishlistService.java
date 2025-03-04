package com.shoppingmall.product.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Wishlist;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.WishlistRepository;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Wishlist addToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        return wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseGet(() -> wishlistRepository.save(Wishlist.builder()
                    .user(user)
                    .product(product)
                    .createdAt(LocalDateTime.now())
                    .build()));
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
        wishlistRepository.delete(wishlist);
    }

    public List<Wishlist> getWishlistsByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }
}
