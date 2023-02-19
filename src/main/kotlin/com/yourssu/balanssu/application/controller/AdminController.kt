package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.CreateCategoryRequest
import com.yourssu.balanssu.domain.model.dto.CreateItemDto
import com.yourssu.balanssu.domain.service.CategoryService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/admin")
class AdminController(private val categoryService: CategoryService) {
    @ApiOperation("category 생성")
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(
        @RequestBody @Valid request: CreateCategoryRequest,
    ) {
        val itemDtos = request.items.map { itemRequest ->
            CreateItemDto(itemRequest.name, itemRequest.filename, itemRequest.base64.split(",")[1])
        }
        categoryService.createCategory(request.title, itemDtos)
    }
}