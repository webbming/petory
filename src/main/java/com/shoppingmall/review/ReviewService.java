package com.shoppingmall.review;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingmall.product.Product;
import com.shoppingmall.product.ProductRepository;

@Service
public class ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private ProductRepository productRepository;
	
	public void saveReview(Review review) {
        reviewRepository.save(review);

        // 해당 상품의 평균 평점 업데이트
        Long productId = review.getProductId();
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
}
