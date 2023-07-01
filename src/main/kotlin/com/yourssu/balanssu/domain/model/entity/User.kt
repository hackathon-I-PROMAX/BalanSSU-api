package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.security.UserRole
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Where
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
@DynamicUpdate
@Where(clause = "is_deleted = 0")
class User(
    @Column(unique = true)
    val username: String,

    val password: String,

    @Column(unique = true)
    val nickname: String,

    val schoolAge: String,

    val departure: String,

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
