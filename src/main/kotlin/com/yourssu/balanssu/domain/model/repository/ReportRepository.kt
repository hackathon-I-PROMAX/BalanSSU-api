package com.yourssu.balanssu.domain.model.repository

import com.yourssu.balanssu.domain.model.entity.Comment
import com.yourssu.balanssu.domain.model.entity.Report
import com.yourssu.balanssu.domain.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<Report, Int> {
    fun existsByUserAndComment(user: User, comment: Comment): Boolean

    fun findByUserAndComment(user: User, comment: Comment): Report?
}
