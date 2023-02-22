package com.yourssu.balanssu.domain.model.dto

import com.yourssu.balanssu.domain.model.enums.CategoryType

class ViewCategoriesDto(
    val categoryId: String,
    val title: String,
    val type: CategoryType,
    val dDay: Int,
    val participantCount: Int
)
