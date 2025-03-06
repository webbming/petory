package com.shoppingmall.cart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;



import org.springframework.stereotype.Service;

import com.shoppingmall.cart.controller.CartController;
import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.model.CartItem;
import com.shoppingmall.cart.repository.CartItemRepository;
import com.shoppingmall.cart.repository.CartRepository;

import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.service.ProductService;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    
   
    // 장바구니 추가
    public Cart addCart(String userId) {
        User user = userRepository.findByUserId(userId);
        if(user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        return cartRepository.save(new Cart(user));
    }

 // 활성화된 장바구니 조회
    public Optional<Cart> getActiveCart(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 userId를 가진 사용자가 존재하지 않습니다.");
        }
        return cartRepository.findByUserAndIsActiveTrue(user).stream().findFirst();
    }


    // 장바구니가 없다면 새로 생성
    public Cart createCart(String userId) {
        return getActiveCart(userId).orElseGet(() -> {
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                throw new IllegalArgumentException("해당 userId를 가진 사용자가 존재하지 않습니다.");
            }
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        });
    }


    // 장바구니 삭제
    public void removeCart(String userId, Long id) {
        Cart cart = cartRepository.findByIdAndUser(id, null)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));
        cartRepository.delete(cart);
    }
    
    @Transactional
    public void addOrUpdateCartItem(String userId, Long id, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        // 사용자 정보 조회
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 장바구니 존재 여부 및 본인 소유 여부 확인
        Cart cart = cartRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));
        
        // 상품 존재 여부 확인
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다.");
        }
        
        // 장바구니에 해당 상품이 있는지 확인
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            // 장바구니에 해당 상품이 있을 경우 수량 업데이트
            updateCartItemQuantity(cartItem, quantity);
        } else {
            // 장바구니에 해당 상품이 없으면 새로 추가
            addCartItem(cart, product, quantity);
        }
    }

    private void addCartItem(Cart cart, Product product, int quantity) {
        // 새로운 장바구니 아이템 생성
        CartItem newCartItem = new CartItem(cart, product, quantity, product.getPrice());
        cartItemRepository.save(newCartItem);
    }

    private void updateCartItemQuantity(CartItem cartItem, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        
        // 수량을 더하는 방식으로 변경
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        // 가격 업데이트가 필요한 경우 여기서도 처리 가능 (예: 할인 적용 등)
        cartItemRepository.save(cartItem);
    }


    // 장바구니 총 금액 계산
    public BigDecimal calculateTotalPrice(String userId, Long id) {
        Cart cart = cartRepository.findByIdAndUser(id, null)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));

        return cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 장바구니 아이템 가격 업데이트
    public void updateCartItemPrices(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product updatedProduct = productService.getProductById(cartItem.getProduct().getProductId());
            cartItem.setPrice(updatedProduct.getPrice());
        }
        cartItemRepository.saveAll(cartItems);
    }
}
