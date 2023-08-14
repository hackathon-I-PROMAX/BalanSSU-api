package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.CreateCategoryRequest
import com.yourssu.balanssu.domain.model.dto.CreateChoiceDto
import com.yourssu.balanssu.domain.service.CategoryService
import com.yourssu.balanssu.domain.service.CommentService
import io.swagger.annotations.ApiOperation
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController(
    private val categoryService: CategoryService,
    private val commentService: CommentService
) {
    @ApiOperation("category 생성")
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(
        @RequestBody @Valid request: CreateCategoryRequest,
    ) {
        val choices = request.choices.map { CreateChoiceDto(it.name) }
        categoryService.createCategory(request.title, choices)
    }

    @ApiOperation("댓글 신고 처리")
    @DeleteMapping("/categories/{categoryId}/comments/{commentId}/reports")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun reportComment(
        @PathVariable categoryId: String,
        @PathVariable commentId: String
    ) = commentService.reportComment(categoryId, commentId)
}
