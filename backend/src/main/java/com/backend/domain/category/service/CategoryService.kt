package com.backend.domain.category.service;

import static com.backend.domain.category.converter.CategoryConverter.toEntity;
import static com.backend.domain.category.converter.CategoryConverter.toResponse;
import static com.backend.domain.category.converter.CategoryConverter.toResponseList;

import com.backend.domain.category.dto.request.CategoryRequest;
import com.backend.domain.category.dto.response.CategoryResponse;
import com.backend.domain.category.entity.Category;
import com.backend.domain.category.repository.CategoryRepository;
import com.backend.global.exception.GlobalErrorCode;
import com.backend.global.exception.GlobalException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 전체 조회
    public List<CategoryResponse> categoryList() {
        List<Category> categoryList = categoryRepository.findAll();
        return toResponseList(categoryList);
    }

    @Transactional
    // 카테고리 추가 (관리자만 등록 가능)
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {

        // 중복 검사
        categoryNameCheck(categoryRequest.getName());

        // DTO -> Entity로 변환
        Category category = toEntity(categoryRequest);

        // DB에 저장
        Category saveCategory = categoryRepository.save(category);

        // 응답 객체로 변환 후 반환
        return toResponse(saveCategory);
    }

    @Transactional
    // 카테고리 수정 (관리자만 가능)
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {

        // 중복 검사
        categoryNameCheck(categoryRequest.getName());

        // 기존 카테고리 id로 조회, 없으면 NOT_FOUND 예외 처리
        Category findCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND));

        // 더티 체킹으로 값 변경
        findCategory.updateName(categoryRequest.getName());

        // 응답 객체로 변환 후 반환
        return toResponse(findCategory);
    }

    @Transactional
    // 카테고리 삭제
    public void deleteCategory(Long id) {

        // 기존 카테고리 id로 조회, 없으면 NOT_FOUND 예외 처리
        Category findCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.CATEGORY_NOT_FOUND));

        categoryRepository.delete(findCategory);
    }

    // 중복 검사 메서드
    public void categoryNameCheck(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new GlobalException(GlobalErrorCode.DUPLICATED_CATEGORY_NAME);
        }
    }
}