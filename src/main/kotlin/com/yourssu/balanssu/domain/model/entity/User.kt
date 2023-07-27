package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.security.UserRole
import javax.persistence.*
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table
@DynamicUpdate
class User(
    @Column(unique = true)
    val username: String,

    val password: String,

    @Column(unique = true)
    val nickname: String,

    val schoolAge: String,

    @Column(columnDefinition = "CHAR(4)")
    val mbti: String,

    val gender: Char
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    lateinit var refreshToken: String

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.ROLE_USER

    var isDeleted: Boolean = false

    fun renewRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}
