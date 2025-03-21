package com.shoppingmall.board.dto;

import com.shoppingmall.board.model.Board;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BoardSpec {
    public static Specification<Board> hasCategory(String categoryId) {
        return (root, query, cb) ->
                (categoryId == null || "all".equals(categoryId)) ? null : cb.equal(root.get("categoryId"), categoryId);
    }

    public static Specification<Board> hasPostType(PostType postType) {
        return (root, query, cb) ->
                (postType == null) ? null : cb.equal(root.get("postType"), postType);
    }

    public static Specification<Board> createdAfter(LocalDateTime startDate) {
        return (root, query, cb) ->
                (startDate == null) ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
    }

    public static Specification<Board> titleContains(String search) {
        return (root, query, cb) ->
                (search == null || search.isEmpty()) ? null : cb.like(root.get("title"), "%" + search + "%");
    }
}
