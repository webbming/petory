package com.shoppingmall.order.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class PurchaseReview {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewId;

  private int rating;

  private String comment;

  @ElementCollection
  private List<String> imagePaths;
}
