package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Int>
