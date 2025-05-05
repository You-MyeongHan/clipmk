package com.clipmk;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.clipmk.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
    
    private final BoardService boardService;
    
    @GetMapping("/")
    public String index(Model model, @PageableDefault(page=0, size=20, sort={"id"}) Pageable pageable) {
        // 게시글 목록을 가져와서 모델에 추가
        
        model.addAttribute("posts", boardService.findAll(pageable));
        return "view/main/index";
    }

	@GetMapping("/post/{id}")
	public String post(Model model, @PathVariable("id") Long id){

		model.addAttribute("post", boardService.getPostById(id));

		return "view/board/post";
	}

    // //베스트 게시물 
	// @GetMapping("/posts")
	// public ResponseEntity<Page<PostsResDto>> bestPosts(
	// 		@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
		
	// 	Page<PostsResDto> boards =null;
	// 	boards = boardService.findByViewCntGreaterThan(viewCount, pageable).map(PostsResDto::from);
	// 	return ResponseEntity.ok(boards);
	// }

    // //게시물 페이징 and 검색
	// @GetMapping("/posts/{table}")
	// public ResponseEntity<Page<PostsResDto>> posts(
	// 		@PageableDefault(page=0, size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
	// 		@PathVariable("table") String table,
	// 		@RequestParam(value="group", defaultValue  = "") String group,
	// 		@RequestParam(value = "searchTerm", defaultValue  = "") String searchTerm
	// 		){
		
	// 	Page<PostsResDto> posts = boardService.findAll(PostSpecifications.filterPosts(table, searchTerm, group), pageable)
	// 			.map(PostsResDto::from);
		
	// 	return ResponseEntity.ok(posts);
	// }
}