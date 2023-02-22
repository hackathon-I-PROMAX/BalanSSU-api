package com.yourssu.balanssu.application.response

import com.yourssu.balanssu.domain.model.dto.CategoryDto
import com.yourssu.balanssu.domain.model.dto.ChoiceDto
import com.yourssu.balanssu.domain.model.dto.CommentDto

class ViewCategoryResponse(
    val category: CategoryDto,
    val choices: List<ChoiceDto>,
    val comments: List<CommentDto>
)
