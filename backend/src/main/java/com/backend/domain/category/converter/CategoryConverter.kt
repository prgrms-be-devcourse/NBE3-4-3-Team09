package com.backend.domain.category.converter;

import com.backend.domain.category.dto.request.CategoryRequest;
import com.backend.domain.category.dto.response.CategoryResponse;
import com.backend.domain.category.entity.Category;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    // 카테고리 리스트 매핑
    public static List<CategoryResponse> toResponseList(List<Category> categoryList) {
        return categoryList.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getCreatedAt(),
                        category.getModifiedAt()
                ))
                .collect(Collectors.toList());
    }

    // 카테고리 매핑 (단일 객체)
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getModifiedAt()
        );
    }

    // 카테고리 엔티티로 바꾸는 메서드
    public static Category toEntity(CategoryRequest categoryRequest) {
        return new Category(categoryRequest.getName());
    }
}
