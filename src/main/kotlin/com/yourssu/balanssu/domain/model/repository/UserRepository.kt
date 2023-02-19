package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun existsByUsernameOrNickname(username: String, nickname: String): Boolean

    fun findByUsername(username: String): User?

    fun existsByUsernameAndRefreshToken(username: String, refreshToken: String): Boolean

    fun existsByUsername(username: String): Boolean
}
