package com.clipmk.barter.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.clipmk.barter.entity.Item;

import jakarta.persistence.criteria.Predicate;

public class ItemSpecifications {
	public static Specification<Item> filterItems(String category, String searchTerm, String regionCode){
		return(root, query, criteriaBuilder)->{
			List<Predicate> predicates = new ArrayList<>();
			
			if (!category.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }
			
			if (!searchTerm.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + searchTerm + "%"));
            }
			
			if (!regionCode.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("regionCode"), regionCode));
            }
			
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
