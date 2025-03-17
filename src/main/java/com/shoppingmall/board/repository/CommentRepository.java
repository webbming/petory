package com.shoppingmall.board.repository;

import java.util.List;

import com.shoppingmall.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shoppingmall.board.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	//전체 최신순 정렬
	List<Comment> findByBoard_BoardIdOrderByCommentId(Long boardId);
	
	//댓글 수
	int countByBoardBoardId(Long boardId);

	Page<Comment> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
