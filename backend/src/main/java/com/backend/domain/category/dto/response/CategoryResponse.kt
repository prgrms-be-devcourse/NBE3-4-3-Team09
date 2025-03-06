package com.backend.domain.category.dto.response

import java.time.ZonedDateTime

data class CategoryResponse(
    val id: Long ?= 0L,
    val name: String = "",
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val modifiedAt: ZonedDateTime = ZonedDateTime.now()
)
