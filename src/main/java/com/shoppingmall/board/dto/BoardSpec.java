package com.shoppingmall.board.dto;

import com.shoppingmall.board.model.Board;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Set;

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

    public static Specification<Board> hasHashtag(String hashtag) {
        return (root, query, cb) -> {
            if (hashtag == null || hashtag.isEmpty()) {
                return null;
            }

            // "hashtag" 필드가 Set<String>일 때 LIKE 연산자를 사용하여 해시태그가 포함된 게시물 찾기
            return cb.like(cb.lower(root.get("hashtag").as(String.class)), "%" + hashtag.toLowerCase() + "%");
        };
    }
}
