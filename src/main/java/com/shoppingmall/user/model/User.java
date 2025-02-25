package com.shoppingmall.user.model;

import com.shoppingmall.user.dto.UserResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email", "accountType"})
})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true, nullable = false)
  private String userId;
  @Column(length = 60, nullable = false)
  private String password;
  @Column(nullable = false)
  private String email;
  @Column(unique = true, nullable = false)
  private String nickname;
  private String question;
  private String answer;
  private String address;
  private String accountType;
  @Enumerated(EnumType.STRING)
  private UserRoleType role;


  public UserResponseDTO toDTO() {
    return UserResponseDTO.builder()
        .userId(userId)
        .email(email)
        .nickname(nickname)
        .address(address)
        .accountType(accountType)
        .role(role)
        .build();
  }
}


