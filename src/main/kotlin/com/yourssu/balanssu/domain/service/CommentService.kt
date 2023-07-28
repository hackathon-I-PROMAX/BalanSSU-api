package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.domain.exception.CannotDeleteCommentException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.CommentNotFoundException
import com.yourssu.balanssu.domain.exception.RestrictedUserException
import com.yourssu.balanssu.domain.model.dto.CommentDto
import com.yourssu.balanssu.domain.model.entity.Comment
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.CommentRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val commentRepository: CommentRepository
) {
    fun createComment(username: String, categoryId: String, content: String) {
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val user = userRepository.findByUsernameAndIsDeletedIsFalse(username) ?: throw RestrictedUserException()
        val comment = Comment(category, user, content)
        commentRepository.save(comment)
    }

    fun getComments(username: String, categoryId: String, page: Int, size: Int): Page<CommentDto> {
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val user = userRepository.findByUsernameAndIsDeletedIsFalse(username) ?: throw RestrictedUserException()

        val pageable = PageRequest.of(page, size, Sort.by("id"))
        return commentRepository.findAllByCategory(category, pageable).map { comment ->
            val writer = comment.user
            CommentDto(
                writer.nickname,
                writer.mbti!!,
                comment.clientId,
                comment.content,
                writer == user,
                writer.isDeleted
            )
        }
    }

    fun deleteComment(username: String, categoryId: String, commentId: String) {
        val category = categoryRepository.findByClientId(categoryId) ?: throw CategoryNotFoundException()
        val comment =
            commentRepository.findByClientIdAndCategory(commentId, category) ?: throw CommentNotFoundException()
        val user = userRepository.findByUsernameAndIsDeletedIsFalse(username) ?: throw RestrictedUserException()
        if (comment.user != user) {
            throw CannotDeleteCommentException()
        }
        comment.delete()
    }
}
