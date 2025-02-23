package com.shoppingmall.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.model.Comment;
import com.shoppingmall.board.repository.CommentRepository;

@Service
public class CommentService {
	@Autowired
	CommentRepository repository;
	String toggle = "minus";
	
	//댓글 조회
	public List<Comment> getComment(int boardId) {
		return repository.findByBoard_BoardIdOrderByCommentId(boardId);
	}
	
	//댓글 등록
	public void saveComment(Comment comment) {
		repository.save(comment);
	}
	
	//id 검색
	public Comment getCommentById(int commentId) {
		Comment comment = repository.findById(commentId).orElse(null);
	    return repository.save(comment);
	}
	
	//게시글 좋아요
	public void likeComment(int commentId) {
		Comment comment = repository.findById(commentId).orElse(null);
		int likeCount = comment.getLikeCount();
		if(toggle.equals("minus")) {
			likeCount++;
			toggle = "plus";
		}
		else {
			likeCount--;
			toggle = "minus";
		}
		comment.setLikeCount(likeCount);
	    repository.save(comment);
	}
	
	//댓글 삭제
	public void deleteComment(int commentId) {
		repository.deleteById(commentId);
	}
	
	//댓글 수
	public int countComment(int boardId) {
		return repository.countByBoardBoardId(boardId);
	}
}
