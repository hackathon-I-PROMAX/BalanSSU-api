package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.entity.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Int> {
    fun findAllByCategory(category: Category, pageRequest: PageRequest): Page<Comment>
}
