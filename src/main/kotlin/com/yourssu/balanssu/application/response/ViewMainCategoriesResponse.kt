package com.yourssu.balanssu.application.response

import com.yourssu.balanssu.domain.model.dto.ViewCategoriesDto

class ViewMainCategoriesResponse(
    val hotCategories: List<ViewCategoriesDto>,
    val closedCategories: List<ViewCategoriesDto>
)
