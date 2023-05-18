package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.model.entity.Comment
import com.yourssu.balanssu.domain.model.repository.CategoryRepository
import com.yourssu.balanssu.domain.model.repository.CommentRepository
import com.yourssu.balanssu.domain.model.repository.UserRepository
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
        val user = userRepository.findByUsername(username)!!
        val comment = Comment(category, user, content)
        commentRepository.save(comment)
    }
}
