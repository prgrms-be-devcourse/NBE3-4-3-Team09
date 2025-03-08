package com.backend.global.baseentity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.ZonedDateTime;

/**
 * BaseEntity
 * <p>엔티티 생성, 수정 일자를 관리하는 BaseEntity 입니다.</p>
 * @author Kim Dong O
 */
@MappedSuperclass
abstract class BaseEntity {

	/**
	 * 생성일시
	 */
	@Column(name = "created_at")
	lateinit var createdAt: ZonedDateTime
		protected set

	/**
	 * 수정일시
	 */
	@Column(name = "modified_at")
	lateinit var modifiedAt: ZonedDateTime
		protected set

	@PrePersist
	fun prePersist() {
		this.createdAt = ZonedDateTime.now();
		this.modifiedAt = ZonedDateTime.now();
	}

	@PreUpdate
	fun preUpdate() {
		this.modifiedAt = ZonedDateTime.now();
	}
}