package com.shoppingmall.exhandler;

import com.shoppingmall.cart.service.CartService;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
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
    public void addAttributes(HttpServletRequest request, Model model , Authentication authentication) {
        String username = authentication.getName();
        String uri = request.getRequestURI();
        if (uri.startsWith("http://localhost:8080/users/me")) {
            // "/users/me" 하위 경로에 대해서만 모델 데이터 추가
            User user = userRepository.findByUserId(username);
            model.addAttribute("userInfo", user.toDTO());
            System.out.println("잘 담았답니다" + model.getAttribute("userInfo"));
        }
    }
}
