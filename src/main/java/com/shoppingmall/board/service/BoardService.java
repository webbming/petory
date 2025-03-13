package com.shoppingmall.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
	public Page<Board> getPostByKeyword(String keyword, String category, String order, String bydate, LocalDateTime startDate, String hashtag, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		List<Board> boardList = repository.searchBoards(keyword, category, order, bydate, startDate);
		if(!hashtag.equals("all")) {
			List<Board> returnList = new ArrayList<Board>();
			boardList.forEach(board->{
				if(board.getHashtag().contains(hashtag)) {
					returnList.add(board);
				}
			});
			int start = Math.min((int) pageable.getOffset(), returnList.size());
		    int end = Math.min((start + pageable.getPageSize()), returnList.size());
			return new PageImpl<Board>(returnList.subList(start, end), pageable, returnList.size());
		}
		else {
			int start = Math.min((int) pageable.getOffset(), boardList.size());
		    int end = Math.min((start + pageable.getPageSize()), boardList.size());
			return new PageImpl<Board>(boardList.subList(start, end), pageable, boardList.size());
		}
	}
	
	//날짜 검색
	public LocalDateTime getStartDateForPeriod(String bydate) {
        LocalDateTime now = LocalDateTime.now();
        switch (bydate) {
            case "1개월":
                return now.minusMonths(1);
            case "3개월":
                return now.minusMonths(3);
            case "전체":
                return null;
            default:
                throw new IllegalArgumentException("Invalid period");
        }
    }
	
	//상세조회
	public Board viewPost(Long boardId, User user) {
		Board board = repository.findById(boardId).orElse(null);
		Set<Long> container = board.getViewContain();
		
		if(!container.contains(user.getId())) {
			container.add(user.getId());
			board.setViewContain(container);
			int viewCount = board.getViewCount();
			viewCount++;
			board.setViewCount(viewCount);
			board.setViewContain(container);
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
	public int likePost(Long boardId, User user) {
		Board board = repository.findById(boardId).orElse(null);
		Set<Long> container = board.getLikeContain();
		
		if(!container.contains(user.getId())) {
			container.add(user.getId());
			board.setLikeContain(container);
			int likeCount = board.getLikeCount();
			likeCount++;
			board.setLikeCount(likeCount);
			repository.save(board);
			return likeCount;
		}
		else {
			container.remove(user.getId());
			board.setLikeContain(container);
			int likeCount = board.getLikeCount();
			likeCount--;
			board.setLikeCount(likeCount);
			repository.save(board);
			return likeCount;
		}
	}
	
	//수정
	public Board updatePost(Long boardId, String title, String content, String categoryId, Set<String> hashtag) {
		Board board = repository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Invalid board Id"));
		board.setTitle(title);
		board.setContent(content);
		board.setCategoryId(categoryId);
		board.setHashtag(hashtag);
		return repository.save(board);
	}
	
	//삭제
	public void deletePost(Long board_id) {
		repository.deleteById(board_id);
	}
}
