package com.backend.domain.category.service

import com.backend.domain.category.converter.CategoryConverter.toEntity
import com.backend.domain.category.converter.CategoryConverter.toResponse
import com.backend.domain.category.converter.CategoryConverter.toResponseList
import com.backend.domain.category.dto.request.CategoryRequest
import com.backend.domain.category.dto.response.CategoryResponse
import com.backend.domain.category.repository.CategoryRepository
import com.backend.global.exception.GlobalErrorCode
import com.backend.global.exception.GlobalException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    // 카테고리 전체 조회
    fun categoryList(): List<CategoryResponse> {
        val categoryList = categoryRepository.findAll()
        return toResponseList(categoryList)
    }

    @Transactional
    // 카테고리 추가
    fun createCategory(categoryRequest: CategoryRequest): CategoryResponse {

        categoryNameCheck(categoryRequest.name)

        val saveCategory = categoryRepository.save(toEntity(categoryRequest))

        return toResponse(saveCategory)
    }

    @Transactional
    // 카테고리 수정
    fun updateCategory(id: Long, categoryRequest: CategoryRequest): CategoryResponse {

        categoryNameCheck(categoryRequest.name)

        val findCategory = categoryRepository.findById(id)
            .orElseThrow { GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND) }

        findCategory.updateName(categoryRequest.name)

        return toResponse(findCategory)
    }

    @Transactional
    // 카테고리 삭제
    fun deleteCategory(id: Long) {

        val findCategory = categoryRepository.findById(id)
            .orElseThrow { GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND) }

        categoryRepository.delete(findCategory)
    }

    // 중복 검사 메서드
    private fun categoryNameCheck(name: String) {
        if (categoryRepository.existsByName(name)) {
            throw GlobalException(GlobalErrorCode.DUPLICATED_CATEGORY_NAME)
        }
    }
}