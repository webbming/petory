package com.shoppingmall.review;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ProductRepository productRepository;
	
	public void saveReview(Review review, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadFile(imageFile);
            if (imageUrl != null) {
                review.setImageUrl(imageUrl);
            }
        }
        reviewRepository.save(review);
        updateProductRating(review.getProductId());
    }

	 private String uploadFile(MultipartFile file) {
	        if (file != null && !file.isEmpty()) {
	            try {
	                String basePath = new File("src/main/resources/static/images").getAbsolutePath();
	                String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
	                File destinationFile = new File(basePath, fileName);
	                file.transferTo(destinationFile);
	                return "/images/" + fileName;
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return null;
	    }

	private void updateProductRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        BigDecimal avgRating = reviews.stream()
                .map(Review::getRating)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(reviews.size()), 2, RoundingMode.HALF_UP);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        product.setAverageRating(avgRating);
        productRepository.save(product);
    }
	
	public List<Review> getReviewsByProductId(Long productId) {
		return reviewRepository.findByProductId(productId);
	}
	
	public void addComment(Long reviewId, String content, Long userId) throws JsonProcessingException {
	    Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
	    List<Map<String, Object>> commentsList;

	    if (review.getComments() != null && !review.getComments().isEmpty()) {
	        commentsList = new ObjectMapper().readValue(review.getComments(), new TypeReference<List<Map<String, Object>>>() {});
	    } else {
	        commentsList = new ArrayList<>();
	    }

	    // 새로운 댓글 추가
	    Map<String, Object> newComment = new HashMap<>();
	    newComment.put("userId", userId);
	    newComment.put("content", content);
	    newComment.put("createdAt", new Date());
	    commentsList.add(newComment);

	    String updatedComments = new ObjectMapper().writeValueAsString(commentsList);
	    review.setComments(updatedComments);
	    
	    reviewRepository.save(review);

	    // ✅ 디버깅 로그 추가
	    System.out.println("리뷰 ID: " + reviewId + "에 댓글 추가됨: " + updatedComments);
	}


    public List<Map<String, Object>> getComments(Long reviewId) throws JsonProcessingException {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found"));
        if (review.getComments() != null && !review.getComments().isEmpty()) {
            return new ObjectMapper().readValue(review.getComments(), new TypeReference<>() {});
        }
        return new ArrayList<>();
    }
}
