package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.ReportCommentRequest
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import com.yourssu.balanssu.domain.service.ReportService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories/{categoryId}/comments/{commentId}/reports")
class ReportController(
    private val reportService: ReportService
) {
    @ApiOperation("댓글 신고")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun reportComment(
        @Authenticated userInfo: UserInfo,
        @PathVariable commentId: String,
        @RequestBody request: ReportCommentRequest
    ) = reportService.reportComment(userInfo.username, commentId, request.type, request.content, request.email)
}
