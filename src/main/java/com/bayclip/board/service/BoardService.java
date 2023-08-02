package com.bayclip.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bayclip.board.dto.EditPostRequestDto;
import com.bayclip.board.dto.PostDto;
import com.bayclip.board.dto.PostRequestDto;
import com.bayclip.board.entity.Comment;
import com.bayclip.board.entity.Post;
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
	public PostDto getPostById(Long postId, User user) {
		Post post=boardRepository.findWithUserNickById(postId);
		
		if(post!=null) {
			post.setViewCnt(post.getViewCnt()+1);
			post=boardRepository.save(post);
			
			PostDto postDto = post.toDto();
			
			if(user!=null) {
				postDto.setRecommend_state(post.getRecommendations().contains(user.getId()));
				postDto.setRecommend_state(post.getDecommendations().contains(user.getId()));
			}
			return postDto;	        
		}
		return null;
        
	}
	
//	public Long getBoardCnt(Integer userId) {
//		
//		return boardRepository.countByUser_Id(userId);
//	}
	
	@Transactional
	public PostDto edit(Long postId, EditPostRequestDto request, User user) {
		
		Post post= boardRepository.findById(postId).orElse(null);
		
		if(user!=null) {
			String newContent=request.getContent();
			
			if(newContent != null) {
				post.setContent(newContent);
			}
			boardRepository.save(post);
			
			return getPostById(postId, user);

		}
		
		return null;
	}
	
	@Transactional
	public boolean delete(Long postId, User user) {
		
		Post post=boardRepository.findById(postId).orElse(null);
		
		if(user!=null) {
			if(post!=null) {
				boardRepository.delete(post);
				return true;
			}
		}
		return false;
	}
	
	public Page<Post> findAll(Pageable pageable, String category) {
		Specification<Post> spec = null;
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
	
	public Page<Post> findByView_cntGreaterThan(Integer viewCount, Pageable pageable){
		return boardRepository.findByViewCntGreaterThan(viewCount, pageable);
	}
	
	public Page<Post> findByTitleContaining(Pageable pageable, String searchKeyword){
		return boardRepository.findByTitleContaining(pageable, searchKeyword);
	}
	
	public Page<Post> findTop10ByUser_IdOrderByIdDesc(Integer userId, Pageable pageable){
		return boardRepository.findTop10ByUser_IdOrderByIdDesc(userId, pageable);
	}
	
	@Transactional
	public boolean register(PostRequestDto request, Integer user_id) {
		User user=userRepository.findById(user_id).orElseThrow(()->
				new IllegalStateException("존재하지 않는 계정입니다."));
		var post=Post.builder()
				.title(request.getTitle())
				.category(request.getCategory())
				.content(request.getContent())
				.user(user)
				.nick(user.getNick())
				.thumbnail(request.getThumbnail())
				.build();
		boardRepository.save(post);
		
		return true;
	}
	
	@Transactional
	public void updateView_cnt(Long postId) {
		Post post=boardRepository.findById(postId).orElseThrow(()->
			new IllegalStateException("게시물이 존재하지 않습니다."));
		
		post.updateViewCnt(post.getViewCnt());
	}
	
	public Boolean recommendBoard(Long postId, User user, int value) {
		
		Post post = boardRepository.findById(postId).orElse(null);
		if(post!=null) {
			if(user!=null) {
				if( value==1) {
					
					if(post.getDecommendations().contains(user.getId())) {
						post.getDecommendations().remove(user.getId());
						boardRepository.save(post);
						return true;
					}
					
					if(post.getRecommendations().contains(user.getId())) {
						return false;
					}
					
					post.getRecommendations().add(user.getId());
					boardRepository.save(post);
					return true;
					
				}
				else if(value==0) {
					
					if(post.getRecommendations().contains(user.getId())) {
						post.getRecommendations().remove(user.getId());
						boardRepository.save(post);
						return true;
					}
					
					if(post.getDecommendations().contains(user.getId())) {
						return false;
					}
					
					post.getDecommendations().add(user.getId());
					boardRepository.save(post);
					return true;
				}
			}
		}
		
		return false;
	}
}