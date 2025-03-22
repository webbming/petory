package com.shoppingmall.board.service;

import com.shoppingmall.board.dto.BoardRequestDTO;
import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.board.dto.BoardSpec;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import static com.shoppingmall.board.dto.BoardRequestDTO.extractAndSaveHashtags;

@Service
public class BoardService {
	private final BoardRepository repository;
	private final UserRepository userRepository;
	private final UserService userService;

	@Autowired
	public BoardService(BoardRepository repository, UserRepository userRepository,
			UserService userService) {
		this.repository = repository;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	//저장
	@Transactional
	public Board savePost(Board board){

		return repository.save(board);
    }

	// 역할에 따른 게시글 조회
	public Page<Board> getPosts(BoardRequestDTO.Search dto) {
		Sort sort = "인기순".equals(dto.getSort())
				? Sort.by(Sort.Direction.DESC, "likeCount")
				: Sort.by(Sort.Direction.DESC, "createdAt");
		// 페이지와 정렬 기준 설정

		Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), sort);

		// 기간 설정
		LocalDateTime startDate = (dto.getPeriod() != null && !"all".equals(dto.getPeriod()))
				? getStartDateForPeriod(dto.getPeriod())
				: null;

		Specification<Board> spec = Specification.where(BoardSpec.hasCategory(dto.getCategoryId()))
				.and(BoardSpec.hasPostType(dto.getPostType()))
				.and(BoardSpec.createdAfter(startDate))
				.and(BoardSpec.titleContains(dto.getSearch()));
		return repository.findAll(spec , pageable);
	}


	// 인기순 조회 (스크롤 페이징)
	public List<Board> getAllPostsSortedByLikes(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Board> boardPage = repository.findAllSortedByLikes(pageable);
		return boardPage.getContent();
	}

	// 메인화면용 상위 9개 게시글 조회 (최신순/인기순/조회순/공지사항)
	public List<BoardResponseDTO> getBoardContent(String type) {
		if("best".equals(type)) {
			return repository.findTop9ByOrderByLikeCountDesc()
					.stream()
					.map(Board::toDTO)
					.toList();

		}else if("view".equals(type)) {
			return repository.findTop9ByOrderByViewCountDesc()
					.stream()
					.map(Board::toDTO)
					.toList();
		} else if("공지".equals(type)) {
			return repository.findTop9ByCategoryIdOrderByCreatedAtDesc("공지").stream()
					.map(Board :: toDTO).toList();
		}
		else {
			return repository.findTop9ByOrderByCreatedAtDesc()
					.stream()
					.map(Board::toDTO)
					.toList();
		}
	}
	
	//검색
	public Page<Board> getPostByKeyword(String keyword, String category, String orderby, String bydate, LocalDateTime startDate, String hashtag, int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		List<Board> boardList = repository.searchBoards(keyword, category, orderby, bydate, startDate);
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
	public String getNickname(String userId) {
		return userService.getUser(userId).getNickname();
	}
	
	//게시글 좋아요
	public synchronized Integer likePost(Long boardId, User user) {
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
	public void updatePost(BoardRequestDTO.Update boardRequestDTO) {
		Board board = repository.findById(boardRequestDTO.getBoardId()).orElseThrow(() -> new IllegalArgumentException("Invalid board Id"));
		board.setTitle(boardRequestDTO.getTitle());
		board.setContent(board.getContent());
		board.setCategoryId(board.getCategoryId());
		board.setHashtag(extractAndSaveHashtags(boardRequestDTO.getHashtag()));
		repository.save(board);
	}
	
	//삭제
	public void deletePost(Long board_id) {
		repository.deleteById(board_id);
	}


	// 모든 게시물을 인기순으로 스크롤을 내릴때마다 5개씩

}
