package com.shoppingmall.board.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.repository.BoardRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository repository;

	String toggle = "minus";
	
	//저장
	public void savePost(Board board){
		repository.save(board);
    }
	
	//전체 검색
	public Page<Board> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByOrderByBoardIdDesc(pageable);
    }
	
	//검색
	public Page<Board> getPostByKeyword(String keyword, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return repository.searchBoards(keyword, pageable);
	}
	
	//상세조회(조회수는 session이용 한 명당 1씩만 증가)
	public Board getPostById(Long boardId) {
		Board board = repository.findById(boardId).orElse(null);
		int viewCount = board.getViewCount();
		viewCount++;
		board.setViewCount(viewCount);
        return repository.save(board);
    }
	
	//게시글 좋아요
	public void likePost(Long boardId) {
		Board board = repository.findById(boardId).orElse(null);
		int likeCount = board.getLikeCount();
		if(toggle.equals("minus")) {
			likeCount++;
			toggle = "plus";
		}
		else {
			likeCount--;
			toggle = "minus";
		}
		board.setLikeCount(likeCount);
        repository.save(board);
	}
	
	//수정
	public Board updatePost(Long boardId, String title, String content, String categoryId) {
		Board board = repository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Invalid board Id"));
		board.setTitle(title);
		board.setContent(content);
		board.setCategoryId(categoryId);
		return repository.save(board);
	}
	
	//삭제
	public void deletePost(Long board_id) {
		repository.deleteById(board_id);
	}
}
