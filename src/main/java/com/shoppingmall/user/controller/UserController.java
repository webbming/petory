package com.shoppingmall.user.controller;

import com.shoppingmall.config.security.CustomUserDetails;
import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.UserService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {

  private final UserRepository userRepository;
  private UserService userService;
  public UserController(UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }


  // 회원가입 페이지
  @GetMapping("/register")
  public String registerG(){
    return "user/register";
  }

  // 회원가입 필드별 유효성 검사
  @PostMapping("/register/check")
  @ResponseBody
  public ResponseEntity<String> checkDuplicate(@RequestBody Map<String , String> request){
    System.out.println("요청옴");

    System.out.println(request);
    return ResponseEntity.ok("gimori");
  }

  // 회원가입 요청 및 최종 검사
  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> registerP(@Valid @RequestBody UserRequestDTO userDTO , Errors errors){
    Map<String , Object> response = new HashMap<>();
    if(errors.hasErrors()){
      Map<String , String> errorMap = new HashMap<>();
      for(FieldError error : errors.getFieldErrors()){
        errorMap.put(error.getField(), error.getDefaultMessage());
      }
      response.put("status", "error");
      response.put("errors",errorMap);
      return ResponseEntity.badRequest().body(response);
    }

    userService.registerUser(userDTO);
    response.put("userId" , userDTO.getUserId());
    response.put("status", "success");
    return ResponseEntity.ok().body(response);
  }

  @PatchMapping("/user/update")
  public ResponseEntity<Map<String, Object>> UpdateUser(@Valid @ModelAttribute UserRequestDTO userDTO , Errors errors){

    return null;

  }

  @DeleteMapping("/user/delete")
  public String DeleteUser(@Valid @ModelAttribute UserRequestDTO userDTO){
    return null;
  }

  @GetMapping("/user/profile")
  public String profileG(){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    System.out.println(auth);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    String address = userDetails.getAddress();
    System.out.println(address);
    return "user/profile";
  }



//  @GetMapping("/profile")
//  public String profileG(){
//
//    userService.getUser()
//    return
//  }

}
