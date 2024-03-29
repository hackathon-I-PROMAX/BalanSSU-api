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
    @Column(unique = true, columnDefinition = "VARCHAR(64)", nullable = false)
    val username: String,

    @Column(columnDefinition = "CHAR(60)")
    var password: String?,

    @Column(unique = true, columnDefinition = "VARCHAR(64)", nullable = false)
    val nickname: String,

    @Column(columnDefinition = "CHAR(2)")
    var schoolAge: String?,

    @Column(columnDefinition = "CHAR(4)")
    var mbti: String?,

    @Column(columnDefinition = "CHAR(1)")
    var gender: Char?
) {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null

    lateinit var refreshToken: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserRole = UserRole.ROLE_USER

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    val createdAt = LocalDateTime.now(Clock.systemDefaultZone())

    @Column(columnDefinition = "BIT(1)", nullable = false)
    var isDeleted: Boolean = false

    @Column(columnDefinition = "DATETIME")
    var deletedAt: LocalDateTime? = null

    fun renewRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }

    fun delete() {
        this.password = null
        this.schoolAge = null
        this.mbti = null
        this.gender = null
        this.role = UserRole.ROLE_DELETED_USER
        this.isDeleted = true
        this.deletedAt = LocalDateTime.now(Clock.systemDefaultZone())
    }
}
