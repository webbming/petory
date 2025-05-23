package com.shoppingmall.config.security;

import com.shoppingmall.user.model.User;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// 유저의 정보를 시큐리티가 식별 가능한 타입으로 되돌려 주기 위한 class
public class CustomUserDetails implements UserDetails {

  private final User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  public final User getUser() {
    return user;
  }

  public String getEmail() {
    return user.getEmail(); // 이메일 반환
  }

  public String getAccountType(){
    return user.getAccountType();
  }

  public String getNickname() {
    return user.getNickname();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
   return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }


  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUserId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
