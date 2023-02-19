package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.VoteCategoryRequest
import com.yourssu.balanssu.application.response.VoteCategoryResponse
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import com.yourssu.balanssu.domain.service.CategoryService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {
    @ApiOperation("선택지 투표")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun voteCategory(
        @Authenticated userInfo: UserInfo,
        @RequestBody request: VoteCategoryRequest
    ): VoteCategoryResponse {
        val votes = categoryService.voteCategory(userInfo.username, request.categoryId, request.itemId)
        return VoteCategoryResponse(votes)
    }
}
