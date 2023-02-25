package com.yourssu.balanssu.domain.model.dto

class ViewCategoryDto(
    val category: CategoryDto,
    val choices: List<ChoiceDto>,
    val comments: List<CommentDto>
)
