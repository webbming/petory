package com.shoppingmall.board.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.model.Comment;
import com.shoppingmall.board.repository.BoardRepository;
import com.shoppingmall.board.repository.CommentRepository;
import com.shoppingmall.board.service.BoardService;
import com.shoppingmall.board.service.CommentService;
import com.shoppingmall.user.model.User;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private CommentService commentService;
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	//리스트 조회
	@GetMapping("/board")
    public String getBoardByKeyword(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "2") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           Model model) {
		Page<Board> board = boardService.getPostByKeyword(keyword, page, size);
		
		board.forEach(i -> {
			Long boardId = i.getBoardId();
			i.setCommentCount(commentService.countComment(boardId));
		});
		
        model.addAttribute("board", board);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "board/board";
    }
	
	//작성페이지 이동
	@GetMapping("/write")
	public String writePostPage() {
		return "board/write";
	}
	
	//등록
	@PostMapping("/write")
	public String writePost(@RequestParam("file") MultipartFile file, @ModelAttribute Board board, @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam("videoFile") MultipartFile videoFile, Model model) throws IllegalStateException, IOException {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
        File targetFile = new File(uploadDir + File.separator + filename);
        
        if (!imageFile.isEmpty()) {
            String imagePath = saveFile(imageFile);
            model.addAttribute("imagePath", imagePath);
        }

        // 동영상 파일 처리
        if (!videoFile.isEmpty()) {
            String videoPath = saveFile(videoFile);
            model.addAttribute("videoPath", videoPath);
        }

        // 파일을 지정된 위치에 저장
        file.transferTo(targetFile);

		boardService.savePost(board);
		model.addAttribute("board", board);
		return "board/read";
	}
	
	private String saveFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        Path path = Paths.get(uploadDir + File.separator + filename);
        Files.createDirectories(path.getParent()); // 부모 디렉토리가 없으면 생성
        file.transferTo(path); // 파일 저장
        return path.toString();
    }
	
	//상세페이지 이동
	@GetMapping("/read")
	public String readPost(@RequestParam("boardId") Long boardId, Model model) {
		Board board = boardService.getPostById(boardId);
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
			Model model) {
		boardService.updatePost(boardId, title, content, categoryId);
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
	public String likePost(@RequestParam("boardId") Long boardId) {
		boardService.likePost(boardId);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 좋아요
	@GetMapping("/commentLike")
	public String likeComment(@RequestParam("commentId") Long commentId, @RequestParam("boardId") Long boardId) {
		commentService.likeComment(commentId);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 등록
	@GetMapping("/commentCreate")
	public String commentCreate(@RequestParam("content") String content, @RequestParam("boardId") Long boardId) {
		Comment comment = new Comment();
		Board board = boardService.getPostById(boardId);
		comment.setContent(content);
		comment.setBoard(board);
		comment.setUserId(board.getUserId());
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
