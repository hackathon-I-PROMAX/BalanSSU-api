package com.yourssu.balanssu.application.request

import com.yourssu.balanssu.domain.model.dto.CategoryDto
import com.yourssu.balanssu.domain.model.dto.ChoiceDto

class MakeChoiceResponse(
    val category: CategoryDto,
    val choices: List<ChoiceDto>
)
