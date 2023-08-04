package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.domain.exception.CannotReportOwnCommentException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.CommentAlreadyReportedException
import com.yourssu.balanssu.domain.exception.CommentNotFoundException
import com.yourssu.balanssu.domain.model.dto.ReportAvailableDto
import com.yourssu.balanssu.domain.model.entity.Report
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.CommentRepository
import com.yourssu.balanssu.domain.model.repository.ReportRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReportService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val commentRepository: CommentRepository,
    private val reportRepository: ReportRepository
) {
    fun reportComment(
        username: String,
        categoryId: String,
        commentId: String,
        type: String,
        content: String?,
        email: String
    ) {
        val user = userRepository.findByUsername(username)
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val comment =
            commentRepository.findByClientIdAndCategory(commentId, category) ?: throw CommentNotFoundException()

        if (comment.user == user) {
            throw CannotReportOwnCommentException()
        } else if (reportRepository.existsByUserAndComment(user, comment)) {
            throw CommentAlreadyReportedException()
        }

        val report = Report(user, comment, type, content, email)
        reportRepository.save(report)
    }

    fun isReportAvailable(
        username: String,
        categoryId: String,
        commentId: String
    ): ReportAvailableDto {
        val user = userRepository.findByUsername(username)
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val comment =
            commentRepository.findByClientIdAndCategory(commentId, category) ?: throw CommentNotFoundException()

        val isAvailable = comment.user != user && !reportRepository.existsByUserAndComment(user, comment)
        return ReportAvailableDto(isAvailable)
    }
}
