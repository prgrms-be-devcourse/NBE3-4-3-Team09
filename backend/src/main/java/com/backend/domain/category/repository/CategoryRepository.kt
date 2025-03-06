package com.backend.domain.category.repository

import com.backend.domain.category.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CategoryRepository : JpaRepository<Category, Long> {

    // 중복 검사
    fun existsByName(name: String): Boolean

    fun findByName(categoryName: String): Optional<Category>
}
