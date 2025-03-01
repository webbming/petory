package com.shoppingmall.user.controller;

import com.shoppingmall.user.dto.MypageTopInfoDTO;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.EmailService;
import com.shoppingmall.user.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@Controller
@RequestMapping("/users")
public class UserController {

	private final UserRepository userRepository;
	private UserService userService;
	private EmailService emailService;

	public UserController(UserService userService, UserRepository userRepository,
			EmailService emailService) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.emailService = emailService;
	}

	@GetMapping("/agree")
	public String agreeG() {
		return "user/agree";
	}

	// 회원가입 페이지
	@GetMapping
	public String registerG(@RequestParam(name = "agree", defaultValue = "false") boolean agree) {
		if (!agree) {
			return "redirect:/users/agree";
		}
		return "user/register";
	}

	@GetMapping("/me")
	public String profileG(Authentication authentication, HttpSession session) {
		if(authentication == null) {
			return null;
		}
		String userId = authentication.getName();
		System.out.println(userId);
		MypageTopInfoDTO info =  userService.getMyPageTopInfo(userId);
		session.setAttribute("user", info);
		return "user/profile";
	}

	@GetMapping("/me/profile")
	public String profileUpdate(Authentication authentication , HttpSession session) {
		if(authentication == null) {
			return null;
		}
		User user = userRepository.findByUserId(authentication.getName());
		user.setCreatedAt(user.getCreatedAt().withSecond(0).withNano(0));
		session.setAttribute("userinfo", user);
		return "user/profile-userInfo";
	}

	//주소검색 팝업연결
	@GetMapping("/addr")
	public String addrFind() {
		return "user/address";
	}

	@GetMapping("/find/id")
	public String findId() {
		return "user/find-id";
	}

	@GetMapping("/find/pass")
	public String findPassword() {
		return "user/find-password";
	}

}
