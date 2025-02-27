package com.shoppingmall.board.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	//리스트 조회
	@GetMapping("/board")
    public String getBoardByKeyword(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "2") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           @RequestParam(name = "category", defaultValue="") String category,
                           @RequestParam(name = "orderby", defaultValue="최신순") String orderby,
                           @RequestParam(name = "bydate", defaultValue="전체") String bydate,
                           Model model) {
		LocalDateTime startDate = boardService.getStartDateForPeriod(bydate);
		Page<Board> board = boardService.getPostByKeyword(keyword, category, orderby, bydate, startDate, page, size);
		
		board.forEach(i -> {
			Long boardId = i.getBoardId();
			i.setCommentCount(commentService.countComment(boardId));
		});
		
		model.addAttribute("bydate", bydate);
		model.addAttribute("orderby", orderby);
		model.addAttribute("category", category);
        model.addAttribute("board", board);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
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
	public String writePost(@ModelAttribute Board board, Authentication auth, Model model) {
        String nickname = userRepository.findByUserId(auth.getName()).getNickname();
        board.setUser(userRepository.findByUserId(auth.getName()));
        board.setNickname(nickname);

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
			@RequestParam("hashtag") String hashtag,
			Model model) {
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
	@GetMapping("/like")
	public String likePost(Authentication auth, @RequestParam("boardId") Long boardId) {
		User user = userRepository.findByUserId(auth.getName());
		boardService.likePost(boardId, user);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 좋아요
	@GetMapping("/commentLike")
	public String likeComment(Authentication auth, @RequestParam("commentId") Long commentId, @RequestParam("boardId") Long boardId) {
		User user = userRepository.findByUserId(auth.getName());
		commentService.likeComment(commentId, user);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 등록
	@GetMapping("/commentCreate")
	public String commentCreate(Authentication auth, @RequestParam("content") String content, @RequestParam("boardId") Long boardId) {
		User user = userRepository.findByUserId(auth.getName());
		Comment comment = new Comment();
		Board board = boardService.getPostById(boardId);
		comment.setUser(user);
		comment.setNickname(user.getNickname());
		comment.setContent(content);
		comment.setBoard(board);
		comment.setUser(userRepository.findByUserId(board.getUser().getUserId()));
		commentService.saveComment(comment);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 수정
	
	//댓글 삭제
	@GetMapping("/commentDelete")
	public String commentDelete(@RequestParam("commentId") Long commentId, @RequestParam("boardId") Long boardId) {
		commentService.deleteComment(commentId);
		return "redirect:/board/read?boardId=" + boardId;
	}
}
