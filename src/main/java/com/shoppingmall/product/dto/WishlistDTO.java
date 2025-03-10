package com.shoppingmall.product.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDTO {
  private Long id;
  private Long userId;
  private Long productId;
  private LocalDateTime addedOn;
}