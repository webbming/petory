package com.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetRequestDTO {
  private long id;
  private String name;
  private int age;
  private String gender;
  private String birth;
  private String species;
  private String type;

}
