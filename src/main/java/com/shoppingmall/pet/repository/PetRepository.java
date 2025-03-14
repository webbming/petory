package com.shoppingmall.pet.repository;

import com.shoppingmall.pet.model.Pet;
import com.shoppingmall.user.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

  List<Pet> findByUser(User user);
}
