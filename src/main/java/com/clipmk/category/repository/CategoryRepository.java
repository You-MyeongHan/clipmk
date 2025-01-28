package com.clipmk.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clipmk.category.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
