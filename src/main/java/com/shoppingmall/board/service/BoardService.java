package com.shoppingmall.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;

@Service
public class BoardService {
	@Autowired
	private BoardRepository repository;
	private UserRepository userRepository;
	
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
	public Page<Board> getPostByKeyword(String keyword, String category, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return repository.searchBoards(keyword, category, pageable);
	}
	
	//조회수 증가
	
	//상세조회
	public Board viewPost(Long boardId, User user) {
		Board board = repository.findById(boardId).orElse(null);
		List<Long> container = board.getViewContain();
		
		if(!container.contains(user.getId())) {
			List<Long> viewContain = board.getViewContain();
			viewContain.add(user.getId());
			board.setViewContain(viewContain);
			int viewCount = board.getViewCount();
			viewCount++;
			board.setViewCount(viewCount);
			return repository.save(board);
		}
		else {
			return board;
		}
    }
	
	//상세조회
	public Board getPostById(Long boardId) {
		Board board = repository.findById(boardId).orElse(null);
		return board;
	}
	
	//유저 아이디로 닉네임 호출
	public User getNickname(String userId) {
		return userRepository.findByUserId(userId);
	}
	
	//게시글 좋아요
	public void likePost(Long boardId, User user) {
		Board board = repository.findById(boardId).orElse(null);
		System.out.println(board.getUserId());
		List<Long> container = board.getLikeContain();
		
		if(!container.contains(user.getId())) {
			container.add(user.getId());
			board.setLikeContain(container);
			int likeCount = board.getLikeCount();
			likeCount++;
			board.setLikeCount(likeCount);
			repository.save(board);
		}
		else {
			container.remove(user.getId());
			board.setLikeContain(container);
			int likeCount = board.getLikeCount();
			likeCount--;
			board.setLikeCount(likeCount);
			repository.save(board);
		}
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
