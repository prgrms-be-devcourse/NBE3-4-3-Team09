package com.backend.domain.category.controller;

import com.backend.domain.category.dto.request.CategoryRequest;
import com.backend.domain.category.dto.response.CategoryResponse;
import com.backend.domain.category.service.CategoryService;
import com.backend.global.response.GenericResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 전체 조회
    @GetMapping
    public GenericResponse<List<CategoryResponse>> getAllCategory() {
        List<CategoryResponse> categoryList = categoryService.categoryList();
        return GenericResponse.ok(categoryList);
    }

    // 카테고리 추가 (관리자만 가능)
    @PostMapping
    public GenericResponse<CategoryResponse> createCategory(@RequestBody @Validated CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        return GenericResponse.ok(HttpStatus.CREATED.value(), categoryResponse);
    }

    // 카테고리 수정 (관리자만 가능)
    @PatchMapping("/{id}")
    public GenericResponse<CategoryResponse> updateCategory(
            @RequestBody @Validated CategoryRequest categoryRequest, @PathVariable("id") Long id) {
        CategoryResponse categoryResponse = categoryService.updateCategory(id, categoryRequest);
        return GenericResponse.ok(categoryResponse);
    }

    // 카테고리 삭제 (관리자만 가능)
    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return GenericResponse.ok();
    }
}
