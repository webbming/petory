package com.shoppingmall.review;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	
	@Column(nullable = false)
	private Long productId;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false)
	private int rating;
	
	@Column(length = 500)
	private String comment;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt = new Date();

	// 리뷰 이미지 URL 추가
    @Column(name = "image_url")
    private String imageUrl;
    
    // 리뷰 댓글
    @Column(columnDefinition = "TEXT")
    private String comments;
}
