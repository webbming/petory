package com.shoppingmall.user.controller;

import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.EmailService;
import com.shoppingmall.user.service.UserService;

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

	@GetMapping("/profile")
	public String profileG(@RequestParam(name = "agree", defaultValue = "false") boolean agree) {
		return "user/profile";
	}

	@GetMapping("/profile/update")
	public String profileUpdate(@RequestParam(name = "agree", defaultValue = "false") boolean agree) {
		return "user/chageProfile";
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
