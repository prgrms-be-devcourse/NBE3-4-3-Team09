package com.backend.domain.category.repository;

import com.backend.domain.category.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface CategoryRepository : JpaRepository<Category, Long> {

    // 중복 검사
    fun existsByName(name: String): Boolean

    fun findByName(categoryName: String): Optional<Category>
}
