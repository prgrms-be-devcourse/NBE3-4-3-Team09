package com.backend.domain.jobskill.repository;

import com.backend.domain.jobskill.entity.JobSkill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface JobSkillJpaRepository: JpaRepository<JobSkill, Long> {
	@Query("select js from JobSkill js where js.code = :code")
	fun findByCode(@Param("code") code: Int): JobSkill?

//	@Query("SELECT js FROM JobSkill js WHERE js.code = :code")
//	fun findByNameOrNull(name: String): JobSkill?
	@Query("SELECT js FROM JobSkill js WHERE js.name = :name")
	fun findByNameOrNull(@Param("name") name: String): JobSkill?
}
