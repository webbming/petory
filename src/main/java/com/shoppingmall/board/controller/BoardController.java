package com.shoppingmall.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoppingmall.board.model.Board;
import com.shoppingmall.board.model.Comment;
import com.shoppingmall.board.service.BoardService;
import com.shoppingmall.board.service.CommentService;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private CommentService commentService;
	
	//리스트 조회
	@GetMapping("/board")
    public String getBoardByKeyword(@RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "2") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword,
                           Model model) {
		Page<Board> board = boardService.getPostByKeyword(keyword, page, size);
		
		board.forEach(i -> {
			int boardId = i.getBoardId();
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
	public String writePost(@ModelAttribute Board board, Model model) {
		boardService.savePost(board);
		model.addAttribute("board", board);
		return "board/read";
	}
	
	//상세페이지 이동
	@GetMapping("/read")
	public String readPost(@RequestParam("boardId") int boardId, Model model) {
		Board board = boardService.getPostById(boardId);
		List<Comment> comment = commentService.getComment(boardId);
		model.addAttribute("board", board);
		model.addAttribute("comment", comment);
		return "board/read";
	}
	
	//수정페이지 이동
	@GetMapping("/update")
	public String updatePostPage(@RequestParam("boardId") int boardId, Model model) {
		Board board = boardService.getPostById(boardId);
		model.addAttribute("board", board);
		return "board/update";
	}
	
	//수정
	@PostMapping("/update")
	public String updatePost(
			@RequestParam("boardId") int boardId, 
			@RequestParam("title") String title, 
			@RequestParam("content") String content, 
			@RequestParam("categoryId") String categoryId, 
			Model model) {
		boardService.updatePost(boardId, title, content, categoryId);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//삭제
	@GetMapping("/delete")
	public String deletePost(@RequestParam("boardId") int boardId, Model model) {
		boardService.deletePost(boardId);
		return "redirect:/board/board";
	}
	
	//게시글 좋아요
	@GetMapping("/like")
	public String likePost(@RequestParam("boardId") int boardId) {
		boardService.likePost(boardId);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 좋아요
	@GetMapping("/commentLike")
	public String likeComment(@RequestParam("commentId") int commentId, @RequestParam("boardId") int boardId) {
		commentService.likeComment(commentId);
		return "redirect:/board/read?boardId=" + boardId;
	}
	
	//댓글 등록
	@GetMapping("/commentCreate")
	public String commentCreate(@RequestParam("content") String content, @RequestParam("boardId") int boardId) {
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
	public String commentDelete(@RequestParam("commentId") int commentId, @RequestParam("boardId") int boardId) {
		commentService.deleteComment(commentId);
		return "redirect:/board/read?boardId=" + boardId;
	}
}
