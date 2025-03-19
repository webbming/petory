package com.shoppingmall.board.controller;

import com.shoppingmall.board.dto.BoardRequestDTO;
import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.board.repository.BoardRepository;

import com.shoppingmall.user.dto.ApiResponse;
import com.shoppingmall.user.model.UserRoleType;
import com.shoppingmall.user.service.UserService;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.nimbusds.jose.shaded.gson.Gson;
import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.model.Comment;
import com.shoppingmall.board.service.BoardService;
import com.shoppingmall.board.service.CommentService;
import com.shoppingmall.user.model.User;
import com.shoppingmall.user.repository.UserRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
@Tag(name = "Response Estimate", description = "Response Estimate API")
public class BoardController {

	private final BoardService boardService;

	private final CommentService commentService;

	private final UserRepository userRepository;

  private final BoardRepository boardRepository;
	private final UserService userService;

	public BoardController(BoardService boardService , CommentService commentService , UserRepository userRepository , BoardRepository boardRepository,
			UserService userService) {
		this.boardService = boardService;
		this.commentService = commentService;
		this.userRepository = userRepository;
		this.boardRepository = boardRepository;
		this.userService = userService;
	}

	//리스트 조회
	@GetMapping("/board")
    public String getBoardByKeyword(@RequestParam(name = "page", required = false) Integer page,
                           @RequestParam(name = "size", defaultValue = "2") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           @RequestParam(name = "category", defaultValue="") String category,
                           @RequestParam(name = "orderby", defaultValue="최신순") String orderby,
                           @RequestParam(name = "bydate", defaultValue="전체") String bydate,
                           Model model, HttpSession session) {




		Integer sessionPage = (Integer) session.getAttribute("page");
	    if (sessionPage == null) {
	        sessionPage = 0;
	    }
	    
	    if (category != null && !category.isEmpty() && page == null) {
	        session.setAttribute("page", page);
	    } else if (page == null) {
	        page = sessionPage;
	    } else {
	        session.setAttribute("page", page);
	    }
		
		LocalDateTime startDate = boardService.getStartDateForPeriod(bydate);
		Page<Board> board = boardService.getPostByKeyword(keyword, category, orderby, bydate, startDate, page, size);
		
		board.forEach(i -> {
			Pattern pattern = Pattern.compile("<img[^>]*>");
			Matcher matcher = pattern.matcher(i.getContent());
			if(matcher.find()) {
				i.setImage(matcher.group(0)
						.replaceAll("\\s+width=\"[^\"]*\"", "")
                        .replaceAll("\\s+height=\"[^\"]*\"", ""));
			}
			Long boardId = i.getBoardId();
			i.setCommentCount(commentService.countComment(boardId));
			Document doc = Jsoup.parse(i.getContent());
	        
	        Element p = doc.select("p").first();
	        if(p != null) {
	        	i.setContent(p.text());
	        }
		});
		
		
		
		model.addAttribute("bydate", bydate);
		model.addAttribute("orderby", orderby);
		model.addAttribute("category", category);
        model.addAttribute("board", board);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "board/board";
    }
	
	//작성페이지 이동
	@GetMapping("/write")
	public String writePostPage() {

		return "board/write";
	}
	
	//등록
	@PostMapping("/write")
	public String writePost(@ModelAttribute Board board, Authentication auth, Model model) {
    String nickname = userService.getUser(auth.getName()).getNickname();
		boolean isAdmin = auth.getAuthorities().stream()
				.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

		if(board.getCategoryId().equals("시사상식") && !isAdmin) {
			model.addAttribute("error" , "시사상식 카테고리는 관리자만 작성 가능합니다.");
			return "board/write";
		}

		String content = board.getContent();
		String regex = "<img\\s+src=\"([^\"]+)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		board.setUser(userService.getUser(auth.getName()));
    board.setNickname(nickname);

		while (matcher.find()) {
			String src = matcher.group(1); // 첫 번째 그룹이 src 값
			System.out.println("Extracted src: " + src);
			board.setImage(src);
		}
		boardService.savePost(board);
		model.addAttribute("board", board);
		return "board/read";
	}
	
	//이미지 업로드
	@PostMapping("/images")
	@ResponseBody
    public String uploadImage(MultipartHttpServletRequest request, HttpServletRequest req) throws IllegalStateException, IOException {
		// url을 반환하기 위한 hashmap
		Map<String, Object> map = new HashMap<>();

		// 이미지 정보를 받아주는 코드
		MultipartFile uploadFile = request.getFile("upload");

		String originalFileName = uploadFile.getOriginalFilename();

		String ext = originalFileName.substring(originalFileName.indexOf("."));
				
		String uploadDir = System.getProperty("user.dir") + "/uploads";
	    
	    File folder = new File(uploadDir);
	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    String newFileName = UUID.randomUUID() + ext;
	    File file = new File(uploadDir, newFileName);

	    uploadFile.transferTo(file);
				
	    String fileUrl = "/uploads/" + newFileName;
	    map.put("url", fileUrl);
				

		return new Gson().toJson(map);
    }
	
	//상세페이지 이동
	@GetMapping("/read")
	public String readPost(Authentication auth, @RequestParam("boardId") Long boardId, Model model) {
		        User user = userService.getUser(auth.getName());
		        Board board = boardService.viewPost(boardId, user);
		        List<Comment> comment = commentService.getComment(boardId);
		        
		        model.addAttribute("board", board);
		        model.addAttribute("comment", comment);

	    return "board/read";
	}
	
	//수정페이지 이동
	@GetMapping("/update")
	public String updatePostPage(@RequestParam("boardId") Long boardId, Model model) {
		Board board = boardService.getPostById(boardId);
		model.addAttribute("board", board);
		return "board/update";
	}
	
	//수정
	@PostMapping("/update")
	public String updatePost(
			@RequestParam("boardId") Long boardId, 
			@RequestParam("title") String title, 
			@RequestParam("content") String content, 
			@RequestParam("categoryId") String categoryId,
			@RequestParam("hashtag") String hashtag,
			Model model) {
		boardService.updatePost(boardId, title, content, categoryId, hashtag);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//삭제
	@DeleteMapping("/delete")
	@ResponseBody
	public ResponseEntity<?> deletePost(@RequestParam("boardId") Long boardId, Authentication auth) {
		if (auth.isAuthenticated()) {
			// 삭제를 요청하는 사용자
			User user = userService.getUser(auth.getName());

			// 삭제를 요청하는 게시물
			Board board = boardService.getPostById(boardId);

			// 게시물이 존재하는지 확인
			if (board == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물이 존재하지 않습니다."); // 게시물 없음
			}

			// 삭제를 요청하는 게시물의 주인이 삭제를 요청하는 사용자와 같은지 확인
			if (board.getUser().getId().equals(user.getId())) {
				boardService.deletePost(boardId);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다."); // 권한 없음
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증되지 않은 경우
	}
	
	//게시글 좋아요
	@PostMapping("/like")
	@ResponseBody
	public ResponseEntity<ApiResponse<?>> likePost(@RequestBody BoardRequestDTO.Likes likes , Authentication auth) {
		String userId = auth.getName();
		long boardId =  likes.getBoardId();
		User user = userService.getUser(userId);

     Integer likeCount =  boardService.likePost(boardId, user);
		 Map<String, Object> response = new HashMap<>();
		 response.put("likeCount", likeCount);
		return ResponseEntity.ok(ApiResponse.success(response));
	}
	
	//댓글 좋아요
	@GetMapping("/commentLike")
	public String likeComment(Authentication auth, @RequestParam("commentId") Long commentId, @RequestParam("boardId") Long boardId) {
		User user = userService.getUser(auth.getName());
		commentService.likeComment(commentId, user);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	@GetMapping("/hashtag")
	public String hashtag() {
		return "redirect:/board/board";
	}
	
	//댓글 등록
	@GetMapping("/commentCreate")
	public String commentCreate(Authentication auth, @RequestParam("content") String content, @RequestParam("boardId") Long boardId) {
		User user = userService.getUser(auth.getName());
		Comment comment = new Comment();
		Board board = boardService.getPostById(boardId);


		int commentCount = board.getCommentCount() + 1;
		board.setCommentCount(commentCount); // board 객체에 댓글 수 갱신

		comment.setUser(user);
		comment.setNickname(user.getNickname());
		comment.setContent(content);
		comment.setBoard(board);
		comment.setUser(userService.getUser(board.getUser().getUserId()));
		commentService.saveComment(comment);
		boardService.savePost(board);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 수정
	
	//댓글 삭제
	@GetMapping("/commentDelete")
	public String commentDelete(@RequestParam("commentId") Long commentId, @RequestParam("boardId") Long boardId) {
		Board board = boardService.getPostById(boardId);

		int commentCount = board.getCommentCount() - 1;
		board.setCommentCount(commentCount); // board 객체에 댓글 수 갱신

		commentService.deleteComment(commentId);
		boardService.savePost(board);
		return "redirect:/board/read?boardId=" + boardId;
	}


	@GetMapping("/board/list/{type}")
	public ResponseEntity<ApiResponse<?>> boardList(@PathVariable("type") String type) {
		System.out.println(type);
		 List<BoardResponseDTO> boardResponseDTO = boardService.getBoardContent(type);
		 return ResponseEntity.ok(ApiResponse.success(boardResponseDTO));
	}

	@GetMapping("/list")
	public ResponseEntity<ApiResponse<?>> boardList(@RequestParam int page
			,@RequestParam int size
			,@RequestParam(required = false) String sort
			,@RequestParam(required = false) String search
			,@RequestParam(required = false) String period
			,@RequestParam(required = true) String category) {

		List<BoardResponseDTO> boardResponseDTO;

		if (category.equals("시사상식") || category.equals("공지")) {
			// 어드민 게시글 조회 - 데이터베이스에서 바로 필터링
			boardResponseDTO = boardService.getPostsByRole(page, size, category, sort, search, period, UserRoleType.ADMIN)
					.stream()
					.map(Board::toDTO)
					.toList();
		} else {
			// 일반 사용자 게시글 조회 - 데이터베이스에서 바로 필터링
			boardResponseDTO = boardService.getPostsByRole(page, size, category, sort, search, period, UserRoleType.USER)
					.stream()
					.map(Board::toDTO)
					.toList();
		}

		return ResponseEntity.ok(ApiResponse.success(boardResponseDTO));
	}



	@GetMapping("/wiki")
	public String boardPageWiki() {
		System.out.println("요청옴");
		return "board/board-wiki";
	}

	@GetMapping("/main")
	public String boardPageMain() {
		return "board/board-main";
	}

	@GetMapping("/best")
	public String boardPageBest() {
		return "board/board-best";
	}


}
