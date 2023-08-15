package com.bayclip.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.dto.EditCommentRequestDto;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;
import com.bayclip.board.repository.BoardRepository;
import com.bayclip.board.repository.CommentRepository;
import com.bayclip.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
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
    	
    	Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid parent comment ID"));
    	
    	
    	if(post!=null && parentComment!=null) {
    		Comment comment= Comment.builder()
						.content(content)
						.user(user)
						.post(post)
						.parent(parentComment)
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
    	
    	Comment comment=commentRepository.findById(commentId).orElseThrow(null);
    	
    	if(user!=null && comment!=null) {
    		comment.setDel_date(LocalDateTime.now());
        	commentRepository.save(comment);
        	return true;
    	}
    	
    	return false;
    }
    
    @Transactional
	public boolean edit(Long commentId, EditCommentRequestDto request, User user) {
		
		Comment comment= commentRepository.findById(commentId).orElse(null);
		
		if(user!=null && comment!=null) {
			
            if (request.getContent() != null) {
                comment.setContent(request.getContent());
                commentRepository.save(comment);
            }
			
			return true;

		}
		
		return false;
	}
    
    @Transactional
	public Boolean recommend(Long commentId, User user, int value) {
		
		Comment comment= commentRepository.findById(commentId).orElse(null);
		if(comment!=null && user!=null) {
			if( value==1) {
				
				if(comment.getDecommendations().contains(user.getId())) {
					comment.getDecommendations().remove(user.getId());
					commentRepository.save(comment);
					return true;
				}
				
				if(comment.getRecommendations().contains(user.getId())) {
					return false;
				}
				
				comment.getRecommendations().add(user.getId());
				commentRepository.save(comment);
				return true;
				
			}
			else if(value==-1) {
				
				if(comment.getRecommendations().contains(user.getId())) {
					comment.getRecommendations().remove(user.getId());
					commentRepository.save(comment);
					return true;
				}
				
				if(comment.getDecommendations().contains(user.getId())) {
					return false;
				}
				
				comment.getDecommendations().add(user.getId());
				commentRepository.save(comment);
				return true;
			}
		}
		
		return false;
	}
    
}