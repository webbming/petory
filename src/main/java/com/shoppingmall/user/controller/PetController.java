package com.shoppingmall.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pets")
public class PetController {

    @GetMapping
    public String addPet(){

        return "user/pet/addPet";
    }

}
