package com.shoppingmall.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/order/rest")
@RestController
public class PurchaseRestController {

  @PostMapping("/review")
  public ResponseEntity<?> uploadReview(
      @RequestParam("reviewText") String reviewText,
      @RequestParam("reviewImages") List<MultipartFile> reviewImages) {

    // 파일 저장 로직 (예제)
    for (MultipartFile file : reviewImages) {
      System.out.println("파일 이름: " + file.getOriginalFilename());
    }

    System.out.println("리뷰 내용: " + reviewText);

    return ResponseEntity.ok("리뷰 업로드 성공"); // 문자열 반환 (타입이 String)
  }

}
