package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.CreateCommentRequest
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import com.yourssu.balanssu.domain.service.CommentService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/comments")
class CommentController(private val commentService: CommentService) {
    @ApiOperation("댓글 작성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createComment(
        @Authenticated userInfo: UserInfo,
        @RequestBody request: CreateCommentRequest
    ) = commentService.createComment(userInfo.username, request.categoryId, request.content)
}
