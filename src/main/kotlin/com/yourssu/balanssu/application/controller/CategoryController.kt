package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.response.ViewCategoriesResponse
import com.yourssu.balanssu.application.response.ViewCategoryResponse
import com.yourssu.balanssu.application.response.ViewMainCategoriesResponse
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import com.yourssu.balanssu.domain.service.CategoryService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
        return ViewMainCategoriesResponse(categories.hotCategories, categories.closedCategories)
    }

    @ApiOperation("카테고리 목록 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun viewCategories(): ViewCategoriesResponse {
        val categories = categoryService.viewCategories()
        return ViewCategoriesResponse(categories)
    }

    @ApiOperation("카테고리 조회")
    @GetMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    fun viewCategory(
        @Authenticated userInfo: UserInfo,
        @PathVariable categoryId: String
    ): ViewCategoryResponse {
        val category = categoryService.viewCategory(userInfo.username, categoryId)
        return ViewCategoryResponse(
            category = category.category,
            choices = category.choices
        )
    }
}
