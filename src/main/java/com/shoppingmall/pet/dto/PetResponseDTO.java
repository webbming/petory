package com.shoppingmall.pet.dto;

import com.shoppingmall.pet.model.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponseDTO {

  private long id;
  private String name;
  private int age;
  private String gender;
  private String birth;
  private String species;
  private String type;


  public static PetResponseDTO from(Pet pet) {
    return PetResponseDTO.builder()
        .id(pet.getId())
        .name(pet.getName())
        .age(pet.getAge())
        .gender(pet.getGender())
        .birth(pet.getBirth())
        .species(pet.getSpecies())
        .type(pet.getType())
        .build();
  }
}
