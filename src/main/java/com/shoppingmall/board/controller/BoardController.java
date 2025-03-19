package com.shoppingmall.board.controller;

import com.shoppingmall.board.dto.BoardRequestDTO;
import com.shoppingmall.board.dto.BoardResponseDTO;
import com.shoppingmall.board.dto.commentRequestDTO;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.user.dto.ApiResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.apache.bcel.Repository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	@Autowired
	private BoardService boardService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private BoardRepository boardRepository;

	//리스트 조회
	@GetMapping("/board")
    public String getBoardByKeyword(@RequestParam(name = "page", defaultValue="0") Integer page,
                           @RequestParam(name = "size", defaultValue = "2") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           @RequestParam(name = "category", defaultValue="") String category,
                           @RequestParam(name = "orderby", defaultValue="최신순") String order,
                           @RequestParam(name = "bydate", defaultValue="전체") String bydate,
                           @RequestParam(name = "hashtag", defaultValue="all") String hashtag,
                           Model model, HttpSession session) {




		Integer sessionPage = (Integer) session.getAttribute("page");
	    
	    if (category != null && !category.isEmpty() && page == 0) {
	        session.setAttribute("page", page);
	    } else if (page == null) {
	        page = sessionPage;
	    } else {
	        session.setAttribute("page", page);
	    }
		
		LocalDateTime startDate = boardService.getStartDateForPeriod(bydate);
		Page<Board> board = boardService.getPostByKeyword(keyword, category, order, bydate, startDate, hashtag, page, size);
		
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
		
		
		model.addAttribute("hashtag", hashtag);
		model.addAttribute("bydate", bydate);
		model.addAttribute("order", order);
		model.addAttribute("category", category);
        model.addAttribute("board", board);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        return "board/board";
    }
	
	//작성페이지 이동
	@GetMapping("/write")
	public String writePostPage(Authentication auth, Model model) {
		try {
	        if (auth == null || !auth.isAuthenticated()) {
	            return "redirect:/login";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("errorMessage", "게시글을 불러오는 중 오류가 발생했습니다.");
	    }
		return "board/write";
	}
	
	//등록
	@PostMapping("/write")
	public String writePost(@ModelAttribute Board board,
					@RequestParam("hashtags") String hashtags,
					Authentication auth, Model model) {
		board.setHashtag(extractAndSaveHashtags(hashtags));
        String nickname = userRepository.findByUserId(auth.getName()).getNickname();
		String content = board.getContent();
		String regex = "<img\\s+src=\"([^\"]+)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		board.setUser(userRepository.findByUserId(auth.getName()));
        board.setNickname(nickname);

		while (matcher.find()) {
			String src = matcher.group(1); // 첫 번째 그룹이 src 값
			System.out.println("Extracted src: " + src);
			board.setImage(src);
		}
		boardService.savePost(board);
		User user = userRepository.findByUserId(auth.getName());
        Board returnBoard = boardService.viewPost(board.getBoardId(), user);
		model.addAttribute("board", returnBoard);
		model.addAttribute("user", user);
		return "board/read";
	}
	
	private Set<String> extractAndSaveHashtags(String hashtagsInput) {
        Set<String> hashtags = new HashSet<>();
        String[] hashtagArray = hashtagsInput.split("(?=#)");
        String tagName;
        for (String tag : hashtagArray) {
            tag = tag.replaceAll("[^#\\w\\p{IsHangul}]", "");

            tagName = tag.toLowerCase();
            hashtags.add(tagName);
        }
        return hashtags;
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
	    try {
	        if (auth == null || !auth.isAuthenticated()) {
	            return "redirect:/login";
	        }
	        else {
		        User user = userRepository.findByUserId(auth.getName());
		        Board board = boardService.viewPost(boardId, user);
		        List<Comment> comment = commentService.getComment(boardId);
		        
		        model.addAttribute("board", board);
		        model.addAttribute("comment", comment);
		        model.addAttribute("user", user);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("errorMessage", "게시글을 불러오는 중 오류가 발생했습니다.");
	    }
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
			@RequestParam("hashtags") String hashtags,
			Model model) {
		Set<String> hashtag = extractAndSaveHashtags(hashtags);
		boardService.updatePost(boardId, title, content, categoryId, hashtag);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//삭제
	@GetMapping("/delete")
	public String deletePost(@RequestParam("boardId") Long boardId, Model model) {
		boardService.deletePost(boardId);
		return "redirect:/board/board";
	}
	
	//게시글 좋아요
	   @PostMapping("/like")
	   @ResponseBody
	   public ResponseEntity<ApiResponse<?>> likePost(@RequestBody BoardRequestDTO.Likes likes , Authentication auth) {
	      String userId = auth.getName();
	      Long boardId =  likes.getIdAsLong(likes.getBoardId());
	      User user = userRepository.findByUserId(userId);

	     Integer likeCount =  boardService.likePost(boardId, user);
	       Map<String, Object> response = new HashMap<>();
	       response.put("likeCount", likeCount);
	       Board board = boardService.getPostById(boardId);
	       
	       if(board.getLikeContain().contains(likes.getIdAsLong(likes.getUserId()))) {
	    	   response.put("contain", "contained");
	       }
	      return ResponseEntity.ok(ApiResponse.success(response));
	   }
	
	//댓글 좋아요
	@PostMapping("/commentLike")
	@ResponseBody
	public ResponseEntity<ApiResponse<?>> likeComment(Authentication auth, @RequestBody commentRequestDTO.Likes likes) {
		String userId = auth.getName();
		Long commentId = likes.getUserIdAsLong(likes.getCommentId());
		User user = userRepository.findByUserId(userId);
		
		Integer likeCount = commentService.likeComment(commentId, user);
		Map<String, Object> response = new HashMap<>();
		response.put("commentLikeCount", likeCount);
		Comment comment = commentService.getCommentById(commentId);
		
		if(comment.getLikeContain().contains(likes.getUserIdAsLong(likes.getUserId()))) {
	    	   response.put("contain", "contained");
	       }
		
		return ResponseEntity.ok(ApiResponse.success(response));
	}
	
	//댓글 등록
	@GetMapping("/commentCreate")
	public String commentCreate(Authentication auth, @RequestParam("content") String content, @RequestParam("boardId") Long boardId) {
		User user = userRepository.findByUserId(auth.getName());
		Comment comment = new Comment();
		Board board = boardService.getPostById(boardId);


		int commentCount = board.getCommentCount() + 1;
		board.setCommentCount(commentCount); // board 객체에 댓글 수 갱신

		comment.setUser(user);
		comment.setNickname(user.getNickname());
		comment.setContent(content);
		comment.setBoard(board);
		comment.setUser(userRepository.findByUserId(board.getUser().getUserId()));
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
	public ResponseEntity<ApiResponse<?>> boardList(@RequestParam("page") int page
			,@RequestParam("size") int size
			,@RequestParam(name = "sort", required = false) String sort
			,@RequestParam(name = "search", required = false) String search
			,@RequestParam(name = "period", required = false) String period
			,@RequestParam(name = "category", required = true) String category) {

		List<BoardResponseDTO> boardResponseDTO;
		boardResponseDTO = boardService.getAllPosts(page , size , category , sort , search , period)
					.stream().map(Board :: toDTO).toList();

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
