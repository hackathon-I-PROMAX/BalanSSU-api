package com.yourssu.balanssu.domain.model.entity

import com.yourssu.balanssu.core.security.UserRole
import java.time.Clock
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.annotations.DynamicUpdate

@Entity
@Table
@DynamicUpdate
class User(
    @Column(unique = true)
    val username: String,

    var password: String?,

    @Column(unique = true)
    val nickname: String,

    var schoolAge: String?,

    @Column(columnDefinition = "CHAR(4)")
    var mbti: String?,

    var gender: Char?
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    lateinit var refreshToken: String

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.ROLE_USER

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    val createdAt = LocalDateTime.now(Clock.systemDefaultZone())

    var isDeleted: Boolean = false

    @Column(columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = null

    fun renewRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}
