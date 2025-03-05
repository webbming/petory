package com.shoppingmall.user.controller;

import com.shoppingmall.user.dto.ApiResponse;
import com.shoppingmall.user.dto.PetRequestDTO;
import com.shoppingmall.user.model.Pet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PetApiController {

  @PostMapping("/pets")
  public ResponseEntity<ApiResponse<?>> addPet(@RequestBody PetRequestDTO petRequestDTO) {

    System.out.println(petRequestDTO.getAge());
    System.out.println(petRequestDTO.getName());
    System.out.println(petRequestDTO.getType());
    System.out.println(petRequestDTO.getBirth());
    System.out.println(petRequestDTO.getSpecies());
    System.out.println(petRequestDTO.getGender());
    System.out.println("요청옴");
    return ResponseEntity.ok(ApiResponse.success());
  }

}
