package com.shoppingmall.product.dto;


import com.shoppingmall.product.model.Product;
import java.math.BigDecimal;
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
public class ProductResponseDTO {
  private long productId;
  private String productName;
  private BigDecimal price;
  private String option;
  private String content;
  private String description;
  private String imageUrl;
  private int reviewCount;
  private BigDecimal averageRating;

  public static ProductResponseDTO toDTO(Product product) {
    return ProductResponseDTO.builder()
        .productId(product.getProductId())
        .productName(product.getProductName())
        .price(product.getPrice())
        .option(product.getOption())
        .content(product.getContent())
        .description(product.getDescription())
        .reviewCount(product.getReviewCount())
        .imageUrl(product.getImageUrl())
        .averageRating(product.getAverageRating())
        .build();
  }
}
