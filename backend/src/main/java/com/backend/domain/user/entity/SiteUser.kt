package com.backend.domain.user.entity

import com.backend.domain.comment.entity.Comment
import com.backend.domain.jobskill.entity.JobSkill
import com.backend.domain.post.entity.Post
import com.backend.domain.voter.entity.Voter
import com.backend.global.baseentity.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
class SiteUser : BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    var id: Long? = null // 유저 고유 식별 id

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "")
    var email: String = "" // 사용자 이메일

    @Column(name = "password", nullable = false)
    var password: String = "" // 사용자 비밀번호

    @Column(name = "name", nullable = false)
    var name: String = "" // 사용자 이름

    @Column(name = "introduction", nullable = true)
    var introduction: String? = null // 사용자 자기소개

    @Column(name = "job", nullable = true)
    var job: String? = null // 사용자 직무

    @Column(name = "user_role", nullable = false)
    var userRole: String = "ROLE_USER" // 사용자 권한

    var kakaoId: String? = null // 카카오 고유 식별 id

    var profileImg: String? = null // 카카오 프로필 이미지 URL

    @ManyToMany
    @JoinTable(
        name = "user_job_skill",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "job_skill_id")]
    )
    private var _jobSkillList: MutableList<JobSkill> = mutableListOf() // 사용자 직무 스킬

    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL])
    private var _postList: MutableList<Post> = mutableListOf() // 사용자가 작성한 게시글

    @OneToMany(mappedBy = "siteUser", cascade = [CascadeType.ALL])
    private var _commentList: MutableList<Comment> = mutableListOf() // 사용자가 작성한 댓글

    @OneToMany(mappedBy = "siteUser", cascade = [CascadeType.ALL])
    private var _voterList: MutableList<Voter> = mutableListOf()

    // 기본 생성자
    constructor() : super()

    // 필요한 필드만 초기화하는 생성자
    constructor(id: Long?, email: String, userRole: String) : this() {
        this.id = id
        this.email = email
        this.userRole = userRole
    }

    constructor(email: String, name: String, password: String, userRole: String) : this() {
        this.email = email
        this.name = name
        this.password = password
        this.userRole = userRole
    }

    constructor(
        id: Long?, name: String, email: String, kakaoId: String?,
        profileImg: String?, password: String, userRole: String
    ) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.kakaoId = kakaoId
        this.profileImg = profileImg
        this.password = password
        this.userRole = userRole
    }

    constructor(
        name: String, email: String, kakaoId: String?,
        profileImg: String?, password: String, userRole: String
    ) : this() {
        this.name = name
        this.email = email
        this.kakaoId = kakaoId
        this.profileImg = profileImg
        this.password = password
        this.userRole = userRole
    }

    fun modifyProfile(introduction: String?, job: String?) {
        if (introduction != null) this.introduction = introduction
        if (job != null) this.job = job
    }

    fun update(name: String?, profileImg: String?): SiteUser {
        if (name != null) this.name = name
        if (profileImg != null) this.profileImg = profileImg
        return this
    }

    fun updateJobSkills(skills: List<JobSkill>) {
        _jobSkillList.clear()
        _jobSkillList.addAll(skills)
    }

    val jobSkillList: List<JobSkill>
        get() = _jobSkillList.toList()

    val postLIst: List<Post>
        get() = _postList.toList()

    val commentLIst: List<Comment>
        get() = _commentList.toList()

    val voterList: List<Voter>
        get() = _voterList.toList()
}