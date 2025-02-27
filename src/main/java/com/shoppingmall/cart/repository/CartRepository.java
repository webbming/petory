package com.shoppingmall.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.cart.model.Cart;
import com.shoppingmall.user.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByUserAndIsActiveTrue(User user);
    Optional<Cart> findByIdAndUser(Long cartId, User user);
    Optional<Cart> findById(Long Id);
}
