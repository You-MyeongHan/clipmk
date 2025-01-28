package com.clipmk;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.clipmk.board.dto.PostsResDto;
import com.clipmk.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
    
    private final BoardService boardService;
    
    @GetMapping("/")
    public String index(Model model) {
        // 게시글 목록을 가져와서 모델에 추가
        
        model.addAttribute("posts", boardService.getMainPosts());
        return "view/main/index";
    }

    //베스트 게시물 
	@GetMapping("/posts")
	public ResponseEntity<Page<PostsResDto>> bestPosts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
		
		Page<PostsResDto> boards =null;
		boards = boardService.findByViewCntGreaterThan(viewCount, pageable).map(PostsResDto::from);
		return ResponseEntity.ok(boards);
	}

    //게시물 페이징 and 검색
	@GetMapping("/posts/{table}")
	public ResponseEntity<Page<PostsResDto>> posts(
			@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
			@PathVariable("table") String table,
			@RequestParam(value="group", defaultValue  = "") String group,
			@RequestParam(value = "searchTerm", defaultValue  = "") String searchTerm
			){
		
		Page<PostsResDto> posts = boardService.findAll(PostSpecifications.filterPosts(table, searchTerm, group), pageable)
				.map(PostsResDto::from);
		
		return ResponseEntity.ok(posts);
	}
}