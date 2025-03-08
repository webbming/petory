package com.shoppingmall.product.dto;

import com.shoppingmall.product.model.Product;
import com.shoppingmall.product.model.Wishlist;
import com.shoppingmall.user.model.User;
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