package com.shoppingmall.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.shoppingmall.category.Category;
import com.shoppingmall.category.Subcategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;
    private BigDecimal price;
    private String option;
    private String content;
    private String description;
    private String imageUrl;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // **평균 평점 추가**
    @Column(name = "average_rating", precision = 3, scale = 2, nullable = true)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private Subcategory subcategory;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
