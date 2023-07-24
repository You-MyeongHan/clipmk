package com.bayclip.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public boolean register(Long postId, Integer userId, String content) {
    	
    	User user =userRepository.findById(userId).orElseThrow(()->
						new IllegalStateException("존재하지 않는 계정입니다."));
    	
    	if(boardRepository.existsById(postId)) {
    		var comment= Comment.builder()
    					.content(content)
    					.user(user)
    					.nick(user.getNick())
    					.postId(postId)
    					.build();
    		commentRepository.save(comment);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public List<Comment> findByPostId(Long postId){
    	return commentRepository.findByPostId(postId);
    }
    
    public Page<Comment> findTop10ByUser_IdOrderByComment_IdDesc(Integer userId, Pageable pageable){
		return commentRepository.findTop10ByUser_IdOrderByIdDesc(userId, pageable);
	}
    
    @Transactional
    public boolean deleteComment(Long commentId, User user) {
    	
    	Comment comment=commentRepository.findById(commentId)
    			.orElseThrow(()-> new IllegalArgumentException("Comment not found with ID: " + commentId));
    	
    	commentRepository.delete(comment);
    	return true;
    }
}