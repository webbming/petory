package com.shoppingmall.user.service;

import com.shoppingmall.cart.repository.CartRepository;
import com.shoppingmall.user.dto.UserProfileDTO;
import com.shoppingmall.user.dto.UserRequestDTO;
import com.shoppingmall.user.dto.UserResponseDTO;
import com.shoppingmall.user.dto.UserUpdateDTO;
import com.shoppingmall.user.exception.DuplicateException;
import com.shoppingmall.user.exception.FieldErrorsException;
import com.shoppingmall.user.model.Pet;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Service
public class UserService {

  private UserRepository userRepository;
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private CartRepository cartRepository;

  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  // 필드 유효성 검사
  public void filedErrorsHandler(Errors errors) {
    // error 객체 생성
    Map<String, String> errorMap = new HashMap<>();
    if (errors.hasErrors()) {
      for (FieldError error : errors.getFieldErrors()) {
        errorMap.put(error.getField(), error.getDefaultMessage());
      }
      throw new FieldErrorsException(errorMap);
    }
  }

  // 개별 필드 검사
  public boolean checkDuplicate(String fieldName, String fieldValue) {
    // 해당 fieldName 에 맞는 코드 연결 후 fieldValue 검색
    // 프론트엔드는 boolean 값을 받아서 중복이 있는지 없는지 판단 후에 사용자에게 알림.
    boolean isDuplicate = switch (fieldName) {
      case "userId" -> userRepository.existsByUserId(fieldValue);
      case "email" -> userRepository.existsByEmailAndAccountType(fieldValue, "NORMAL");
      case "nickname" -> userRepository.existsByNickname(fieldValue);
      // 해당하는 3가지 필드 중 아무것도 아니라면 예외 처리
      default -> throw new IllegalStateException("Unexpected value: " + fieldName);
    };
    // boolean 값 반환
    return isDuplicate;
  }

  // 회원가입 요청시 최종 중복 검사
  public void checkDuplicate(UserRequestDTO userDTO) {
    // error 를 담을 객체 생성
    Map<String, String> errors = new HashMap<>();
    // 아이디가 이미 있다면 해당 메시지 put
    if (userRepository.existsByUserId(userDTO.getUserId())) {
      errors.put("userId", "이미 사용 중인 아이디입니다.");
    }
    // 이메일이 이미 있다면 해당 메시지 put
    if (userRepository.existsByEmailAndAccountType(userDTO.getEmail(), "NORMAL")) {
      errors.put("email", "이미 사용 중인 이메일입니다.");
    }
    // 닉네임이 이미 있다면 해당 메시지 put
    if (userRepository.existsByNickname(userDTO.getNickname())) {
      errors.put("nickname", "이미 사용 중인 닉네임입니다.");
    }
    // errors 객체에 put 한 메시지가 하나라도 있다면 예외 처리
    if (!errors.isEmpty()) {
      throw new DuplicateException(errors);
    }
  }

  //유저 생성
  @Transactional
  public void registerUser(UserRequestDTO userDTO) {
    // userDTO 의 유저 정보 userId , email , nickname 중복 검사
    // 해당 메서드는 UserRequestDTO 를 인수로 받는 checkDuplicate 메서드 (위에 명시)
    checkDuplicate(userDTO);
    // userDTO 의 유저 정보 비밀번호 암호화
    userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

    // userDTO 를 엔티티로 변환
    User user = userDTO.toEntity();
    user.setAccountType("NORMAL");
    // 유저 저장
    userRepository.save(user);
  }

  // 유저 조회
  public UserResponseDTO getUser(String email, String accountType) {
    // 해당하는 유저 검색
    User user = userRepository.findByEmailAndAccountType(email, accountType);
    // 유저가 없다면 예외처리
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    // 유저 정보 반환
    return user.toDTO();
  }

  public UserProfileDTO getMyPageInfo(String userId){
    User user = userRepository.findByUserId(userId);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    int quantity = user.getCart().getTotalQuantity();
    System.out.println(quantity);
    String nickname = user.getNickname();


    return new UserProfileDTO(nickname , quantity);
  }

  //유저 수정
  @Transactional
  public void updateUser(UserUpdateDTO userDTO) {
    Map<String, String> errors = new HashMap<>();
    // 사용자 아이디 불러오기
    System.out.println(userDTO.getUserId());
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();
    System.out.println(userId);
    // 해당하는 유저 검색
    User user = userRepository.findByUserId(userId);

    // db의 유저 정보와 수정한 유저의 정보가 같지 않고 중복된 이메일이 있을때
    if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(
        userDTO.getEmail())) {
      errors.put("email", "이미 사용 중인 이메일입니다.");
    }
    // db의 유저 정보와 수정한 유저의 정보가 같지 않고 중복된 아이디가 있을때
    if (!user.getNickname().equals(userDTO.getNickname()) && userRepository.existsByNickname(
        userDTO.getNickname())) {
      errors.put("nickname", "이미 사용 중인 닉네임입니다.");
    }
    // 에러가 하나라도 있으면 예외 처리
    if (!errors.isEmpty()) {
      throw new DuplicateException(errors);
    }

    // 유저 정보 갱신
    user.setNickname(userDTO.getNickname());
    user.setEmail(userDTO.getEmail());
    user.setAddress(userDTO.getAddress());
    userRepository.save(user);
  }


  // 유저 삭제
  @Transactional
  public void deleteUser(String userId , String password) {

    User user = userRepository.findByUserId(userId);
    if (user == null) {
      throw new UsernameNotFoundException("회원이 존재 하지 않습니다.");
    }

    if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new IllegalStateException("비밀번호가 일치 하지 않습니다.");
    }

    userRepository.delete(user);

  }

  public String findUserId(String question, String answer) {
    User user = userRepository.findByQuestionAndAnswer(question, answer);
    if (user == null) {
      throw new UsernameNotFoundException("질문과 답변에 일치하는 회원이 없습니다.");
    }
    return user.getUserId();
  }

}
