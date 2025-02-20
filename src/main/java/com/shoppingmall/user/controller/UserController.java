package com.shoppingmall.user.controller;

import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.dto.UserUpdateDTO;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.EmailService;
import com.shoppingmall.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@Controller
@CrossOrigin(origins = "*")
public class UserController {

  private final UserRepository userRepository;
  private UserService userService;
  private EmailService emailService;

  public UserController(UserService userService, UserRepository userRepository , EmailService emailService) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.emailService = emailService;
  }

  @GetMapping("/information")
  public String infoG(){
      return "user/information";
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
    // userService 에게 해당 필드 중복 검사 요청
    boolean result = userService.checkDuplicate(checkField, checkValue);
    // 반환 결과를 response 객체에 넣고 클라이언트에게 반환
    response.put("result" , result);
    return ResponseEntity.ok().body(response);
  }

  // 회원가입 요청 및 최종 검사
  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> registerP(@Valid @RequestBody UserRequestDTO userDTO , Errors errors){
    // response 객체 생성
    Map<String , Object> response = new HashMap<>();
    // 에러가 있다면 response 객체에 status 값과 errors 객체를 클라이언트에게 반환
    if(errors.hasErrors()){
      response.put("status", "error");
      response.put("errors", userService.filedErrorsHandler(errors));
      return ResponseEntity.badRequest().body(response);
    }
    // 에러가 없다면 회원가입 진행
      userService.registerUser(userDTO);
    // 회원가입 성공 시 response 객체에 상태 값과 userId 담아 반환
      response.put("userId" , userDTO.getUserId());
      response.put("status", "success");
      return ResponseEntity.ok().body(response);
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
      userService.updateUser(userDTO);
      response.put("status", "success");
      return ResponseEntity.ok().body(response);
  }


  @DeleteMapping("/user/delete")
  public String DeleteUser(@Valid @ModelAttribute UserRequestDTO userDTO){
    return null;
  }

  @GetMapping("/find/id")
  public String findId(){
    return "user/find-id";
  }

  @PostMapping("/find/id")
  public String findId(@RequestBody Map<String , String> request , Model model){
    String question = request.get("question");
    String answer = request.get("answer");
    System.out.println("question: " + question);
    System.out.println("answer: " + answer);
    String userId = userService.findID(question, answer);
    if(userId != null){
      model.addAttribute("userId" , userId);
    }else{
      model.addAttribute("error" , "조회된 회원이 없습니다.");
    }
    return "/user/findResult";
  }

  @GetMapping("/find/password")
  public String findPassword(){
    return "user/find-password";
  }


  @PostMapping("/find/password")
  @ResponseBody
  public Map<String,String> findPassword(@RequestBody Map<String , String> request) throws MessagingException {
    Map<String,String> response = new HashMap<>();
    String userId = request.get("userId");
    String email = request.get("email");
    emailService.findPassword(userId , email);
    response.put("status", "success");
    response.put("message", "이메일로 임시 비밀번호가 전송 되었습니다. 새로운 비밀번호로 로그인해주세요");
    return response;
  }



//  @GetMapping("/profile")
//  public String profileG(){
//
//    userService.getUser()
//    return
//  }

}
