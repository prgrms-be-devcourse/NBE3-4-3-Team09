package com.backend.domain.jobskill.entity;

import jakarta.persistence.*

@Entity
@Table(name = "job_skill")
class JobSkill {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "job_skill_id")
	var id: Long? = null
		protected set

	@Column(name = "job_skill_code", unique = true, nullable = false)
	var code: Int? = null
		protected set

	@Column(name = "job_skill_name", length = 30, unique = true, nullable = false)
	lateinit var name: String



	constructor(code: Int, name: String) {
		this.code = code
		this.name = name
	}

	constructor(id: Long) {
		this.id = id
	}

}