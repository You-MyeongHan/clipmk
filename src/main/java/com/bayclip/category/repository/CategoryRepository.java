package com.bayclip.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bayclip.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
