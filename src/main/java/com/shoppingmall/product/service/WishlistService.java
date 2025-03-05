package com.shoppingmall.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Wishlist;
import com.shoppingmall.product.repository.ProductRepository;
import com.shoppingmall.product.repository.WishlistRepository;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Wishlist addProductToWishlist(Long userId, Long productId) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new Exception("User not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new Exception("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        return wishlistRepository.save(wishlist);
    }

    public void removeProductFromWishlist(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    public List<Wishlist> getWishlistByUserId(Long userId) {
        return wishlistRepository.findByUserId(userId);
    }
}

