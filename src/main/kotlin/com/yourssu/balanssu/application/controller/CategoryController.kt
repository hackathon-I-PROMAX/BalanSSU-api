package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.ViewChoicesRequest
import com.yourssu.balanssu.application.request.VoteCategoryRequest
import com.yourssu.balanssu.application.response.ViewCategoriesResponse
import com.yourssu.balanssu.application.response.ViewChoiceResponse
import com.yourssu.balanssu.application.response.ViewMainCategoriesResponse
import com.yourssu.balanssu.application.response.VoteCategoryResponse
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import com.yourssu.balanssu.domain.service.CategoryService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
class CategoryController(private val categoryService: CategoryService) {
    @ApiOperation("메인 카테고리 조회")
    @GetMapping("/main")
    @ResponseStatus(HttpStatus.OK)
    fun viewMainCategories(): ViewMainCategoriesResponse {
        val categories = categoryService.viewMainCategories()
        return ViewMainCategoriesResponse(categories.hottestCategories, categories.closedCategories)
    }

    @ApiOperation("카테고리 목록 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun viewCategories(): ViewCategoriesResponse {
        val categories = categoryService.viewCategories()
        return ViewCategoriesResponse(categories)
    }

    @ApiOperation("선택지 투표")
    @PostMapping("/votes")
    @ResponseStatus(HttpStatus.CREATED)
    fun voteCategory(
        @Authenticated userInfo: UserInfo,
        @RequestBody request: VoteCategoryRequest
    ): VoteCategoryResponse {
        val votes = categoryService.voteCategory(userInfo.username, request.categoryId, request.itemId)
        return VoteCategoryResponse(votes)
    }

    @ApiOperation("선택지 조회")
    @GetMapping("/votes")
    @ResponseStatus(HttpStatus.OK)
    fun viewChoices(
        @Authenticated userInfo: UserInfo,
        @RequestBody request: ViewChoicesRequest
    ): ViewChoiceResponse {
        val dto = categoryService.viewChoices(userInfo.username, request.categoryId)
        return ViewChoiceResponse(dto)
    }
}
