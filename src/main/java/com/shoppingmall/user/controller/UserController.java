package com.shoppingmall.user.controller;

import com.shoppingmall.config.security.CustomUserDetails;
import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.dto.UserUpdateDTO;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.UserService;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
  @PostMapping(value = "/register/check", produces = "application/json")
  @ResponseBody
  public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestBody Map<String , String> request){
    Map<String , Boolean> response = new HashMap<>();
    String checkField = request.get("fieldName");
    String checkValue = request.get("fieldValue");
    boolean result = userService.checkDuplicate(checkField, checkValue);
    response.put("result" , result);
    return ResponseEntity.ok().body(response);
  }

  // 회원가입 요청 및 최종 검사
  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> registerP(@Valid @RequestBody UserRequestDTO userDTO , Errors errors){
    Map<String , Object> response = new HashMap<>();
    if(errors.hasErrors()){
      response.put("status", "error");
      response.put("errors", userService.filedErrorsHandler(errors));
      return ResponseEntity.badRequest().body(response);
    }

    try{
      userService.registerUser(userDTO);
      response.put("userId" , userDTO.getUserId());
      response.put("status", "success");
      return ResponseEntity.ok().body(response);
    }catch (IllegalStateException e){
      response.put("status", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }


  @GetMapping("/user/profile")
  public String profileG(Model model){
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String userId = auth.getName();
    UserResponseDTO userInfo =  userService.getUser(userId);
    model.addAttribute("userInfo" , userInfo);
    return "/user/profile";
  }

  @PatchMapping("/user/profile/update")
  public ResponseEntity<Map<String, Object>> UpdateUser(@Valid @RequestBody UserUpdateDTO userDTO , Errors errors){
    Map<String , Object> response = new HashMap<>();
    if(errors.hasErrors()){
      response.put("status", "error");
      response.put("errors", userService.filedErrorsHandler(errors));
      return ResponseEntity.badRequest().body(response);
    }
    try{
      userService.updateUser(userDTO);
      response.put("status", "success");
      return ResponseEntity.ok().body(response);
    }catch (IllegalStateException e){
      response.put("status", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
  }

  @DeleteMapping("/user/delete")
  public String DeleteUser(@Valid @ModelAttribute UserRequestDTO userDTO){
    return null;
  }





//  @GetMapping("/profile")
//  public String profileG(){
//
//    userService.getUser()
//    return
//  }

}
