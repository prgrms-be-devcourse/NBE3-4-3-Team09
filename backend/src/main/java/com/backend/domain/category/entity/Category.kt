package com.backend.domain.category.entity

import com.backend.domain.post.entity.Post
import com.backend.global.baseentity.BaseEntity
import jakarta.persistence.*

@Entity
class Category: BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    @Column(nullable = false, length = 25, unique = true)
    lateinit var name: String
        protected set

    @OneToMany(mappedBy = "category", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    private var _postList: MutableList<Post> = mutableListOf() // 게시글 리스트
    val postList: List<Post>
        get() = _postList.toList()

    constructor(name: String) : super() {
        this.name = name
    }


    // equals() 재정의: id와 name을 기준으로 비교
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    // hashcode() 재정의
    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        return result
    }

    // 카테고리 더티 체킹
    fun updateName(name: String) {
        this.name = name
    }
}
