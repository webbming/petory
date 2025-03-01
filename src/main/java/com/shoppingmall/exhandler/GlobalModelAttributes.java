package com.shoppingmall.exhandler;

import com.shoppingmall.cart.service.CartService;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalModelAttributes {
    private final UserService userService;
    private final UserRepository userRepository;
    private final CartService cartService;

    public GlobalModelAttributes(UserService userService, CartService cartService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.cartService = cartService;
    }

    @ModelAttribute
    public void addAttributes(Model model , Principal principal) {
        if(principal != null) {
            User user = userRepository.findByUserId(principal.getName());
            System.out.println(user.getUserId());
            model.addAttribute("userinfo", user);
            model.addAttribute("cartCount" , user.getCart().getTotalQuantity());
            System.out.println("Model attributes: " + model.asMap().keySet());
        }
    }
}
