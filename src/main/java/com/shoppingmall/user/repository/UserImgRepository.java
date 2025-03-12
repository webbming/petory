package com.shoppingmall.user.repository;

import com.shoppingmall.user.model.User;
import com.shoppingmall.user.model.UserImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImgRepository extends JpaRepository<UserImg, Long> {

  UserImg findByUser(User user);
}
