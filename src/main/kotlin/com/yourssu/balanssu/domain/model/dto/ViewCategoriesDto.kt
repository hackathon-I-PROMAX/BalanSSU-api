package com.yourssu.balanssu.domain.model.dto

import com.yourssu.balanssu.domain.model.entity.Category
import com.yourssu.balanssu.domain.model.enums.CategoryType

class ViewCategoriesDto(
    category: Category,
    val type: CategoryType,
    val dDay: Int
) {
    val clientId: String = category.clientId

    val title: String = category.title

    val participantCount: Int = category.participantCount
}
