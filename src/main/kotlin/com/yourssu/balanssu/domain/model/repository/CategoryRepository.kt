package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Int> {
    fun existsByTitle(title: String): Boolean

    fun findByClientId(clientId: String): Category?
}
