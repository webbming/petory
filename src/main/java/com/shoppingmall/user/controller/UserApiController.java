package com.shoppingmall.user.controller;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.config.security.CustomUserDetails;
import com.shoppingmall.oauth2.model.CustomOAuth2User;
import com.shoppingmall.user.dto.*;
import com.shoppingmall.user.exception.FieldErrorsException;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import com.shoppingmall.user.service.EmailService;
import com.shoppingmall.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

  private final UserRepository userRepository;
  private final UserService userService;
  private final EmailService emailService;

  public UserApiController(UserService userService, UserRepository userRepository,
      EmailService emailService) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.emailService = emailService;

  }

  // 회원가입 필드별 유효성 검사
  @Operation(summary = "중복된 값이 존재하는지 확인하는 컨트롤러", description = "fieldName 과 fieldValue 를 정보로 요청")
  @PostMapping(value = "/check", produces = "application/json")
  public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkDuplicate(
      @RequestBody Map<String, String> request) {

    String checkField = request.get("fieldName");
    String checkValue = request.get("fieldValue");


    boolean result = userService.checkDuplicate(checkField, checkValue);

    Map<String, Boolean> resultMap = Map.of("result", result);
    return ResponseEntity.ok(ApiResponse.success(resultMap));
  }


  // 회원가입 요청 및 최종 검사
  @PostMapping
  @Operation(summary = "회원가입 요청을 처리하는 컨트롤러", description = "usrId , password , email , nickname , question , answer , address 정보로 요청")
  public ResponseEntity<ApiResponse<?>> registerUser(
          @Valid @RequestBody UserRequestDTO userDTO, BindingResult bindingResult) {
    // response 객체 생성
    Map<String, String> response = new HashMap<>();
    if(bindingResult.hasErrors()) {
      filedErrorsHandler(bindingResult);
    }

    userService.registerUser(userDTO );
    response.put("userId", userDTO.getUserId());
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
  }


  @GetMapping("/me/profile/MyPageTopInfo")
  public ResponseEntity<ApiResponse<?>> myPageTopInfo(Authentication authentication) {
    System.out.println("요청옴 탑인포");
        MypageTopInfoDTO dto = userService.getMyPageTopInfo(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(dto));
  }

  @GetMapping("/me/profile")
  @Operation(summary = "회원 정보 조회", description = "인증된 사용자의 정보를 받아와 출력 / 현재 시큐리티 permitAll 때문에 인증이 안된 사용자는 에러페이지 , 인증이 된 사용자만 마이페이지 ")
  public ResponseEntity<?> profileG(Authentication authentication) {
    if(authentication.isAuthenticated()){
      User user = userRepository.findByUserId(authentication.getName());
      return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(user.toDTO()));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("인증되지 않은 사용자"));
  }

  @PostMapping("/me/profile/nickname")
  public ResponseEntity<?> UpdateNickname(@RequestBody Map<String, String> request , Authentication authentication ) {

    String nickname = request.get("nickname");
    userService.userNicknameUpdate(nickname , authentication);
    return ResponseEntity.status(HttpStatus.OK).build();
  }


  @PatchMapping("/me/profile")
  @Operation(summary = "회원 정보 수정(업데이트)", description = "요청시 nickname , email , address 를 정보로 요청")
  public ResponseEntity<Map<String, Object>> UpdateUser(@Valid @RequestBody UserUpdateDTO userDTO,
      BindingResult bindingResult) {
    Map<String, Object> response = new HashMap<>();
    if(bindingResult.hasErrors()) {
      filedErrorsHandler(bindingResult);
    }

    userService.updateUser(userDTO);
    response.put("status", "success");
    return ResponseEntity.ok().body(response);
  }


  @PostMapping("/find/id")
  @Operation(summary = "회원 아이디 찾기", description = "요청시 question 과 answer 정보로 요청")
  public Map<String, String> findId(@RequestBody Map<String, String> request, Model model) {
    Map<String, String> response = new HashMap<>();
    String question = request.get("question");
    String answer = request.get("answer");
    System.out.println(question);
    System.out.println(answer);
    String userId = userService.findUserId(question, answer);
    response.put("status", "success");
    response.put("userId", userId);
    return response;
  }

  @PostMapping("/find/pass")
  @ResponseBody
  @Operation(summary = "회원 비밀번호 찾기", description = "요청시 userId 와 email 정보로 요청")
  public Map<String, String> findPassword(@RequestBody Map<String, String> request)
      throws MessagingException {
    Map<String, String> response = new HashMap<>();
    String userId = request.get("userId");
    String email = request.get("email");
    emailService.findPassword(userId, email);
    response.put("status", "success");
    response.put("message", "이메일로 임시 비밀번호가 전송 되었습니다.<br> 새로운 비밀번호로 로그인해주세요.");
    return response;
  }

  @DeleteMapping
  public ResponseEntity<?> DeleteUser(@RequestBody Map<String, String> password,
      Authentication authentication,
      HttpServletRequest request, HttpServletResponse response) {

    String userPass = password.get("password");
    if (authentication.getPrincipal() instanceof CustomOAuth2User) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (!authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String userId = authentication.getName();
    userService.deleteUser(userId, userPass);

    // 회원 탈퇴후 세션 만료 및 쿠키 삭제 명시적 처리
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.setClearAuthentication(true);
    logoutHandler.setInvalidateHttpSession(true);
    logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("JSESSIONID")) {
          cookie.setValue("");
          cookie.setPath("/");
          cookie.setMaxAge(0);
          response.addCookie(cookie);
          break;
        }
      }
    }

    return ResponseEntity.ok().build();
  }

  @GetMapping("/me/activities/{type}")
  public ResponseEntity<ApiResponse<?>> getActivities(@PathVariable String type , Authentication authentication) {
    System.out.println(type);
      Map<String, Object> response = new HashMap<>();
      if (type.equals("boards")) {
          User user = userService.getCurrentUser(authentication);
          List<Board> boards = user.getBoards();

          List<UserBoardDTO> boardsDtos = user.getBoards().stream()
                          .map(board -> new UserBoardDTO(board.getBoardId() , board.getTitle() , board.getContent())).collect(Collectors.toList());


          response.put("boards" , boardsDtos);



      }
      return ResponseEntity.ok(ApiResponse.success(response));
  }


  public void filedErrorsHandler(BindingResult bindingResult) {
    Map<String, String> errors = new HashMap<>();
    for(FieldError fieldError : bindingResult.getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    throw new FieldErrorsException(errors);
  }

}
