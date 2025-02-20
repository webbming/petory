package com.shoppingmall.cart.service;

import java.math.BigDecimal;
import java.util.List;

import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.model.CartItem;
import com.shoppingmall.user.model.User;
import com.shoppingmall.cart.repository.CartItemRepository;
import com.shoppingmall.cart.repository.CartRepository;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    // 장바구니 추가
    public Cart addCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    // 사용자별 장바구니 조회
    public List<Cart> getCartList(Long userId) {
        List<Cart> cartList = cartRepository.findByUserId(userId);
        if (cartList.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }
        return cartList;
    }

    // 장바구니 삭제
    public void deleteCart(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));
        
        if (!cart.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 장바구니만 삭제할 수 있습니다.");
        }

        cartRepository.delete(cart);
    }

    // 장바구니에 상품 추가 또는 수량 업데이트
    @Transactional
    public void addOrUpdateCartItem(Long userId, Long cartId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        if (!cart.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 장바구니에만 상품을 추가할 수 있습니다.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));

        try {
            cartItemRepository.findByCartCartIdAndProduct_ProductId(cartId, productId)
                    .ifPresentOrElse(
                            cartItem -> {
                                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                                cartItemRepository.save(cartItem);  // JPA가 자동으로 버전 체크
                            },
                            () -> {
                                CartItem newCartItem = new CartItem(cart, product, quantity, product.getPrice());
                                cartItemRepository.save(newCartItem);
                            }
                    );
        } catch (OptimisticLockingFailureException e) {
            throw new IllegalStateException("다른 사용자가 장바구니를 수정 중입니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    // 장바구니 총 금액 계산
    public BigDecimal getTotalPrice(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않습니다."));

        if (!cart.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인의 장바구니 총 금액만 조회할 수 있습니다.");
        }

        return cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 장바구니 아이템의 가격 업데이트
    @Transactional
    public void updateCartItemPrice(CartItem cartItem) {
        Product updatedProduct = productRepository.findById(cartItem.getProduct().getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        
        cartItem.setPrice(updatedProduct.getPrice());
        cartItemRepository.save(cartItem);
    }
}
