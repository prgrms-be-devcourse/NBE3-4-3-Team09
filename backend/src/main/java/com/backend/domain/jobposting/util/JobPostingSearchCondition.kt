package com.backend.domain.jobposting.util

import jakarta.validation.constraints.Min
import lombok.Builder

/**
 * JobPostingSearchCondition
 *
 * 검색 조건을 관리하는 클래스 입니다.
 *
 * @param salaryCode         연봉 코드
 * @param kw                 키워드
 * @param experienceLevel    경력 코드
 * @param requireEducateCode 학력 코드
 * @param sort               정렬 필드
 * @param order              정렬 조건 (오름차, 내림차순)
 * @param pageNum            페이지 번호
 * @param pageSize           페이지 사이즈
 * @author Kim Dong O
 */
@Builder
class JobPostingSearchCondition(
    val salaryCode: Int?,
    val kw: String?,
    val experienceLevel: Int?,
    val requireEducateCode: Int?,
    val sort: String?,
    val order: String?,
    @field:Min(value = 0, message = "음수는 입력할 수 없습니다.")
    val pageNum: Int?,
    @field:Min(value = 1, message = "1 이상의 값을 입력해 주세요")
    val pageSize: Int?
)
