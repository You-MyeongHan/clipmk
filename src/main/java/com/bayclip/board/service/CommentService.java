package com.bayclip.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.entity.Board;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.user.entity.User;
import com.bayclip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public boolean register(Long boardId, Integer userId, String content) {
    	
    	Board board=boardRepository.findById(boardId).orElse(null);
    	User user =userRepository.findById(userId).orElseThrow(()->
    	
		new IllegalStateException("존재하지 않는 계정입니다."));
    	if(board!=null) {
    		var comment= Comment.builder()
    					.content(content)
    					.user(user)
    					.board(board)
    					.build();
    		commentRepository.save(comment);
    	}
    	return true;
    }
    
    public List<Comment> findByBoard(Board board){
    	return commentRepository.findByBoard(board);
    }
    
public Long getCommentCnt(Integer userId) {
		
		return commentRepository.countByUser_Id(userId);
	}
    
    @Transactional
    public void deleteComment(Long commentId) {
    	Comment comment=commentRepository.findById(commentId)
    			.orElseThrow(()-> new IllegalArgumentException("Comment not found with ID: " + commentId));
    	commentRepository.delete(comment);
    }
}