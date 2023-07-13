package com.bayclip.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.entity.Board;
import com.bayclip.board.entity.BoardRequest;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	
	
	@Transactional
	public Board getBoardById(Long boardId) {
		Board board=boardRepository.findWithUserNickById(boardId);
		
		if(board!=null) {
			board.setViewCnt(board.getViewCnt()+1);
			board=boardRepository.save(board);
//			
//			List<Comment> comments = commentRepository.findByBoard(board);
//	        board.setComments(comments);

	        return board;
		}
		return null;
        
	}
	
	public Long getBoardCnt(Integer userId) {
		
		return boardRepository.countByUser_Id(userId);
	}
	
	public Page<Board> findAll(Pageable pageable, String category) {
		Specification<Board> spec = null;
		if(category !=null && !category.isEmpty()) {
			spec = (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("category"), category);
		}
		
		if (spec != null) {
            return boardRepository.findAll(spec, pageable);
        } else {
            return boardRepository.findAll(pageable);
        }
	}
	
	public Page<Board> findByView_cntGreaterThan(Integer viewCount, Pageable pageable){
		return boardRepository.findByViewCntGreaterThan(viewCount, pageable);
	}
	
	public Page<Board> findByTitleContaining(Pageable pageable, String searchKeyword){
		return boardRepository.findByTitleContaining(pageable, searchKeyword);
	}
	
	
	
	@Transactional
	public boolean register(BoardRequest request, Integer user_id) {
		User user=userRepository.findById(user_id).orElseThrow(()->
				new IllegalStateException("존재하지 않는 계정입니다."));
		var board=Board.builder()
				.title(request.getTitle())
				.category(request.getCategory())
				.content(request.getContent())
				.user(user)
				.nick(user.getNick())
				.thumbnail(request.getThumbnail())
				.build();
		boardRepository.save(board);
		
		return true;
	}
	
	@Transactional
	public void updateView_cnt(Long boardId, Board board) {
		Board board1=boardRepository.findById(boardId).orElseThrow(()->
			new IllegalStateException("게시물이 존재하지 않습니다."));
		
		board.updateViewCnt(board1.getViewCnt());
	}
	
	public Boolean recommendBoard(Long boardId, Integer user_id) {
		
		Board board = boardRepository.findById(boardId).orElse(null);
		if(board!=null) {
			User user=userRepository.findById(user_id).orElse(null);
			if(user!=null) {
				if(board.getRecommendations().contains(user)) {
					return false;
				}
				
				board.setRecommend(board.getRecommend()+1);
				board.getRecommendations().add(user);
				boardRepository.save(board);
				return true;
			}
		}
		
		return false;
	}
}