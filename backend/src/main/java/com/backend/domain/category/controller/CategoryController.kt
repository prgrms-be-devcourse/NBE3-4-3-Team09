package com.backend.domain.category.controller

import com.backend.domain.category.dto.request.CategoryRequest
import com.backend.domain.category.dto.response.CategoryResponse
import com.backend.domain.category.service.CategoryService
import com.backend.global.response.GenericResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
class CategoryController(
    private val categoryService: CategoryService
) {

    // 카테고리 전체 조회
    @GetMapping
    fun getAllCategory(): GenericResponse<List<CategoryResponse>> {
        val categoryList = categoryService.categoryList()
        return GenericResponse.ok(categoryList)
    }

    // 카테고리 추가 (관리자만 가능)
    @PostMapping
    fun createCategory(@RequestBody @Validated categoryRequest: CategoryRequest):
            GenericResponse<CategoryResponse>  {
        val categoryResponse = categoryService.createCategory(categoryRequest)
        return GenericResponse.ok(HttpStatus.CREATED.value(), categoryResponse)
    }

    // 카테고리 수정 (관리자만 가능)
    @PatchMapping("/{id}")
    fun updateCategory(
        @PathVariable("id") id: Long,
        @RequestBody @Validated  categoryRequest: CategoryRequest
            ): GenericResponse<CategoryResponse> {
        val categoryResponse = categoryService.updateCategory(id, categoryRequest)
        return GenericResponse.ok(categoryResponse)
    }

    // 카테고리 삭제 (관리자만 가능)
    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable("id") id: Long):  GenericResponse<Void>  {
        categoryService.deleteCategory(id)
        return GenericResponse.ok()
    }
}
