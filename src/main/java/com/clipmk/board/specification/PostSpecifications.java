package com.clipmk.board.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.clipmk.board.entity.Post;

import jakarta.persistence.criteria.Predicate;

public class PostSpecifications {
	public static Specification<Post> filterPosts(String table, String searchTerm, String group){
		return(root, query, criteriaBuilder)->{
			List<Predicate> predicates = new ArrayList<>();
			
			if (!table.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("tbl"), table));
            }
			
			if (!searchTerm.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchTerm + "%"));
            }
			
			if (!group.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("grp"), group));
            }
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
