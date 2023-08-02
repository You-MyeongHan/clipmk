package com.bayclip.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;
import com.bayclip.board.entity.ReplyComment;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.board.repository.ReplyCommentRepository;
import com.bayclip.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final ReplyCommentRepository replyCommentRepository;
    private final BoardRepository boardRepository;
    
    @Transactional
    public boolean createComment(Long postId, User user, String content) {
    	Post post = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID"));
    	if(boardRepository.existsById(postId)) {
    		var comment= Comment.builder()
    					.content(content)
    					.user(user)
    					.post(post)
    					.build();
    		commentRepository.save(comment);
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Transactional
    public boolean createReply(Long postId, Long parentId, User user, String content) {
    	
    	Post post = boardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID"));
    	
    	Comment comment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
    	
    	
    	if(post!=null && comment!=null) {
    		var replyComment= ReplyComment.builder()
						.content(content)
						.user(user)
						.post(post)
						.parentComment(comment)
						.build();
    		
    		replyCommentRepository.save(replyComment);
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