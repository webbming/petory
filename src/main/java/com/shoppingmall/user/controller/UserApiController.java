package com.shoppingmall.user.controller;

import com.shoppingmall.config.security.CustomUserDetails;
import com.shoppingmall.oauth2.dto.OAuth2Response;
import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.dto.UserUpdateDTO;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.EmailService;
import com.shoppingmall.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    public UserApiController(UserService userService , UserRepository userRepository, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;

    }
    // 회원가입 필드별 유효성 검사
    @Operation(summary = "중복된 값이 존재하는지 확인하는 컨트롤러", description = "fieldName 과 fieldValue 를 정보로 요청")
    @PostMapping(value = "/check", produces = "application/json")
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
    @PostMapping
    @Operation(summary = "회원가입 요청을 처리하는 컨트롤러", description = "usrId , password , email , nickname , question , answer , address 정보로 요청")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRequestDTO userDTO , Errors errors)  {
        // response 객체 생성
        Map<String , String> response = new HashMap<>();
        // 에러가 있다면 response 객체에 status 값과 errors 객체를 클라이언트에게 반환
        userService.filedErrorsHandler(errors);
        // 에러가 없다면 회원가입 진행
        userService.registerUser(userDTO);
        // 회원가입 성공 시 response 객체에 상태 값과 userId 담아 반환
        response.put("userId" , userDTO.getUserId());
        response.put("status", "success");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/profile")
    @Operation(summary = "회원 정보 조회", description = "인증된 사용자의 정보를 받아와 출력 / 현재 시큐리티 permitAll 때문에 인증이 안된 사용자는 에러페이지 , 인증이 된 사용자만 마이페이지 ")
    public ResponseEntity<?> profileUser(Authentication authentication , HttpSession session){
        // Principal에서 사용자 정보를 가져옵니다.
        if(session == null || authentication == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof CustomUserDetails) {
            // 일반 사용자
            CustomUserDetails customUserDetails = (CustomUserDetails) principal;
            email = customUserDetails.getEmail();
        } else if (principal instanceof CustomOAuth2User) {
            // 소셜 로그인 사용자
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) principal;
            OAuth2Response oAuth2Response = customOAuth2User.getOAuth2Response();
            email = oAuth2Response.getEmail();
        } else {
            // 기타 사용자 처리 (예: 로그아웃 상태 등)
            throw new RuntimeException("Unknown user type");
        }
        // 이메일을 사용하여 사용자 정보
        UserResponseDTO userInfo = userService.getUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @PatchMapping("/profile")
    @Operation(summary = "회원 정보 수정(업데이트)", description = "요청시 nickname , email , address 를 정보로 요청")
    public ResponseEntity<Map<String, Object>> UpdateUser(@Valid @RequestBody UserUpdateDTO userDTO , Errors errors){
        Map<String , Object> response = new HashMap<>();
        userService.filedErrorsHandler(errors);
        userService.updateUser(userDTO);
        response.put("status", "success");
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/find/id")
    @Operation(summary = "회원 아이디 찾기", description = "요청시 question 과 answer 정보로 요청")
    public Map<String,String> findId(@RequestBody Map<String , String> request , Model model){
        Map<String,String> response = new HashMap<>();
        String question = request.get("question");
        String answer = request.get("answer");
        System.out.println(question);
        System.out.println(answer);
        String userId = userService.findUserId(question, answer);
        response.put("status", "success");
        response.put("userId" , userId);
        return  response;
    }

    @PostMapping("/find/pass")
    @ResponseBody
    @Operation(summary = "회원 비밀번호 찾기", description = "요청시 userId 와 email 정보로 요청")
    public Map<String,String> findPassword(@RequestBody Map<String , String> request) throws MessagingException, MessagingException {
        Map<String,String> response = new HashMap<>();
        String userId = request.get("userId");
        String email = request.get("email");
        emailService.findPassword(userId , email);
        response.put("status", "success");
        response.put("message", "이메일로 임시 비밀번호가 전송 되었습니다. 새로운 비밀번호로 로그인해주세요");
        return response;
    }

    @DeleteMapping
    public String DeleteUser(String password , Authentication auth){

        auth.getName();
        return null;
    }
}
