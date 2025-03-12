package com.shoppingmall.pet.dto;

import com.shoppingmall.pet.model.Pet;
import com.shoppingmall.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetRequestDTO {
  private long id;
  private String name;
  private int age;
  private String gender;
  private String birth;
  private String species;
  private String type;

  public Pet toEntity(User user) {
    return Pet.builder()
        .id(id)
        .name(name)
        .age(age)
        .birth(birth)
        .gender(gender)
        .species(species)
        .user(user)
        .type(type)
        .build();

  }

}
