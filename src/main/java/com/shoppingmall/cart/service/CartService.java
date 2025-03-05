package com.shoppingmall.cart.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    
    // 사용자 조회
    private User getUserById(String userId) {
        User user = userRepository.findByUserId(userId);
        
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }
        return user;
    }

    public Cart getCartByIdAndUser(Long id, User user) {
        // 특정 장바구니 조회
        Optional<Cart> cartOptional = cartRepository.findByIdAndUser(id, user);
        
        if (cartOptional.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 존재하지 않거나 본인의 것이 아닙니다.");
        }
        
        Cart cart = cartOptional.get();
        
        // 로그 추가: 현재 조회한 장바구니 ID 출력
        System.out.println("🛒 조회된 장바구니 ID: " + cart.getId());
        
        // 사용자가 활성화된 장바구니를 하나만 가질 수 있도록 확인
        if (!cart.isActive()) {
            throw new IllegalArgumentException("장바구니가 활성화되지 않았습니다.");
        }

        // 사용자가 활성화된 장바구니를 하나만 가질 수 있도록 확인
        Optional<Cart> activeCart = cartRepository.findByUserAndIsActiveTrue(user);
        if (activeCart.isEmpty() || !activeCart.get().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("사용자는 하나의 장바구니만 가질 수 있습니다.");
        }

        return cart;
    }

    // 상품 조회
    private Product getProductById(Long productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("상품을 찾을 수 없습니다.");
        }
        return product;
    }
    
    public Optional<Cart> getActiveCart(String userId) {
        User user = getUserById(userId);  // 사용자 조회

        // 활성화된 장바구니가 이미 존재하면 반환
        Optional<Cart> activeCart = cartRepository.findByUserAndIsActiveTrue(user);

        if (activeCart.isPresent()) {
            return activeCart;  // 이미 활성화된 장바구니가 있으면 그 장바구니 반환
        }

        // 활성화된 장바구니가 없으면 새 장바구니 생성
        Cart newCart = new Cart(user);
        newCart.setActive(true);  // 새 장바구니 활성화
        cartRepository.save(newCart);  // 새로운 장바구니 DB에 저장

        return Optional.of(newCart);  // 새로 생성한 장바구니 반환
    }

    // 장바구니가 없다면 새로 생성
    public Cart createCart(String userId) {
        return getActiveCart(userId).orElseGet(() -> {
            User user = getUserById(userId);  // 사용자 조회
            Cart newCart = new Cart(user);
            newCart.setActive(true);  // 새 장바구니 활성화
            return cartRepository.save(newCart);
        });
    }
    
    // 장바구니 아이템 추가/수정
    @Transactional
    public void addOrUpdateCartItem(String userId, Long id, Long productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        // 사용자 조회
        User user = getUserById(userId);

        // 장바구니 조회
        Cart cart = getCartByUser(user);

        // 상품 조회
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        // 장바구니에 해당 상품이 있는지 확인
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (cartItem != null) {
        	// 장바구니에 해당 상품이 있을 경우 수량 업데이트
            updateCartItemQuantity(cartItem, quantity); // 수량 업데이트
         
        } else {
            // 장바구니에 해당 상품이 없으면 새로 추가
            addCartItem(user, productId, quantity);  // 새 상품 추가
        }
    }
    
    // 사용자의 장바구니 조회 (기존 장바구니가 없으면 새로 생성)
    @Transactional
    private Cart getCartByUser(User user) {
    	 Optional<Cart> optionalCart = cartRepository.findByUserAndIsActiveTrue(user);

    	    if (optionalCart.isPresent()) {
    	        return optionalCart.get();
    	    }

    	    // 없으면 새로 생성
    	    Cart cart = new Cart(user);
    	    cart.setActive(true);
    	    cartRepository.save(cart);

    	    System.out.println("새 장바구니 생성됨! ID: " + cart.getId() + ", active: " + cart.isActive());

    	    return cart;
    }
    
    // 장바구니 아이템 수량 업데이트
    @Transactional
    private void updateCartItemQuantity(CartItem cartItem, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }

        // 기존 수량을 업데이트하면서 가격 및 총 가격 갱신
        cartItem.setQuantity(quantity);
        
        BigDecimal totalPrice = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity));  // 총 가격 갱신
        
        // 가격 업데이트
        cartItem.setTotalPrice(totalPrice);
        
        cartItemRepository.save(cartItem);  // 수정된 아이템을 DB에 저장
    }
    
    // 새로운 장바구니 아이템 생성
    @Transactional
    public void addCartItem(User user, Long productId, int quantity) {
        // 1. 사용자의 활성화된 장바구니 조회
        Cart cart = cartRepository.findByUserAndIsActiveTrue(user)
                .orElseThrow(() -> new IllegalArgumentException("활성화된 장바구니가 존재하지 않습니다."));

        // 2. 상품 조회
        Product product = productService.getProductById(productId);

        // 3. 이미 장바구니에 동일한 상품이 있는지 확인
        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            // 4. 이미 장바구니에 해당 상품이 있으면 수량만 추가
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            BigDecimal newTotalPrice = existingCartItem.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity()));
            existingCartItem.setTotalPrice(newTotalPrice);
            
            cartItemRepository.save(existingCartItem);  // DB에 저장
            
        } else {
            // 5. 새로 아이템 추가
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            
            BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            cartItem.setTotalPrice(totalPrice);
            cartItem.setPrice(product.getPrice());
            
            cart.getCartItems().add(cartItem);  // cart에 아이템 추가
            cartRepository.save(cart);  // Cart 저장
            cartItemRepository.save(cartItem);  // DB에 저장
        }

        cartItemRepository.flush();  // DB 반영
        System.out.println("장바구니 아이템 추가 완료: " + cart);
    }
    
    // 장바구니 아이템 삭제(개별 삭제)
    @Transactional
    public void removeCartItem(String userId, Long id, Long productId) {
        User user = getUserById(userId);  // 사용자 조회
        Cart cart = getCartByIdAndUser(id, user);  // 장바구니 조회
        Product product = getProductById(productId);  // 상품 조회

        // 장바구니에서 해당 상품이 있는지 확인
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 장바구니에 없습니다."));

        // 상품 삭제
        cart.getCartItems().remove(cartItem); // cart에서 제거
        cartItemRepository.delete(cartItem);
        cartItemRepository.flush(); // 즉시 반영
    }

    // 장바구니 아이템 삭제(다중 선택)
    @Transactional
    public void removeSelectedCartItems(String userId, Long id, List<Long> productIds) {
        User user = getUserById(userId);  // 사용자 조회
        Cart cart = getCartByIdAndUser(id, user);  // 장바구니 조회
        
        // 선택된 상품들을 찾아서 삭제
        List<CartItem> cartItems = cartItemRepository.findByCartAndProduct_ProductIdIn(cart, productIds);

        if(cartItems.isEmpty()) {
        	throw new IllegalArgumentException("해당 상품이 장바구니에 없습니다.");
        }
        System.out.println("조회된 장바구니 아이템: " + cartItems);
        
        cart.getCartItems().removeAll(cartItems); // cart에서 제거
        cartItemRepository.deleteAll(cartItems);
        cartItemRepository.flush(); // 즉시 반영
    }

    // 장바구니 총 금액 계산
    public BigDecimal calculateTotalPrice(String userId, Long id) {
        User user = getUserById(userId);  // 사용자 조회
        Cart cart = getCartByIdAndUser(id, user);  // 장바구니 조회

        return cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // 장바구니에서 선택된 아이템들의 총 가격 계산
    public BigDecimal calculateSelectedItemsTotalPrice(String userId, Long id, List<Long> selectedItemIds) {
        User user = getUserById(userId);
        Cart cart = getCartByIdAndUser(id, user);

        BigDecimal totalPrice = cart.getCartItems().stream()
                .filter(item -> selectedItemIds.contains(item.getId())) // 선택된 아이템만 필터링
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return totalPrice;
    }
    
    // 장바구니 아이템 가격 업데이트
    public void updateCartItemPrices(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product updatedProduct = getProductById(cartItem.getProduct().getProductId());  // 상품 조회
            cartItem.setPrice(updatedProduct.getPrice());
        }
        cartItemRepository.saveAll(cartItems);
    }
}
