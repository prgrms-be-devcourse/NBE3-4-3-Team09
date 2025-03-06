package com.backend.domain.category.converter

import com.backend.domain.category.dto.request.CategoryRequest
import com.backend.domain.category.dto.response.CategoryResponse
import com.backend.domain.category.entity.Category
import org.springframework.stereotype.Component

@Component
object CategoryConverter {

    // 카테고리 리스트 매핑
    fun toResponseList(categoryList : List<Category>): List<CategoryResponse> =
        categoryList.map { category ->
            CategoryResponse(
                id = category.id,
                name = category.name,
                createdAt = category.createdAt,
                modifiedAt = category.modifiedAt
            )
        }

    // 카테고리 매핑 (단일 객체)
    fun toResponse(category: Category): CategoryResponse =
        CategoryResponse(
            id = category.id,
            name = category.name,
            createdAt = category.createdAt,
            modifiedAt = category.modifiedAt
        )

    // 카테고리 엔티티로 바꾸는 메서드
    fun toEntity(categoryRequest: CategoryRequest): Category =
        Category(name = categoryRequest.name)
}
