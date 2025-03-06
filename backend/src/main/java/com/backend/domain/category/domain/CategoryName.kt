package com.backend.domain.category.domain;

import lombok.Getter;

@Getter
public enum CategoryName {
	FREE("자유 게시판"),
	RECRUITMENT("모집 게시판");
	final String value;

	CategoryName(String value) {
		this.value = value;
	}
}
