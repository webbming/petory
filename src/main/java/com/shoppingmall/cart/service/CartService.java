package com.shoppingmall.cart.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.cart.model.CartItem;
import com.shoppingmall.cart.repository.CartItemRepository;
import com.shoppingmall.cart.repository.CartRepository;
import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductService;
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

    // 장바구니 조회 (활성화된 장바구니만 조회)
    public List<Cart> getCartList(String userId) {
    	return cartRepository.findByUser_UserIdAndIsActiveTrue(userId);
    }

    // 장바구니 삭제
    public void deleteCart(String userId, Long cartId) {
        Cart cart = cartRepository.findByCartIdAndUser_UserId(cartId, userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));
        cartRepository.delete(cart);
    }

    // 상품 추가 (이미 있는 상품이면 수량 증가)
    public void addCartItem(String userId, Long cartId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        Cart cart = cartRepository.findByCartIdAndUser_UserId(cartId, userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));
        
        Product product = productService.getProductById(productId);

        cartItemRepository.findByCart_CartIdAndProduct_ProductId(cartId, productId)
                .ifPresentOrElse(
                    cartItem -> cartItem.setQuantity(cartItem.getQuantity() + quantity),
                    () -> cartItemRepository.save(new CartItem(cart, product, quantity, product.getPrice()))
                );
    }

    // 수량 업데이트
    public void updateCartItemQuantity(String userId, Long cartId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cartId, productId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니에 해당 상품이 없습니다."));
        cartItem.setQuantity(quantity); // @Transactional 덕분에 자동 저장됨
    }

    // 장바구니 총 금액 계산
    public BigDecimal getTotalPrice(String userId, Long cartId) {
        Cart cart = cartRepository.findByCartIdAndUser_UserId(cartId, userId)
                .orElseThrow(() -> new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다."));

        return cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 장바구니 아이템의 가격 업데이트
    public void updateCartItemPrices(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product updatedProduct = productService.getProductById(cartItem.getProduct().getProductId());
            cartItem.setPrice(updatedProduct.getPrice());
        }
        cartItemRepository.saveAll(cartItems);
    }
}
