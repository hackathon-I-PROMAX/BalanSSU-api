package com.yourssu.balanssu.application.request

import javax.validation.constraints.Size

class CreateCategoryRequest(
    val title: String,
    @field:Size(min = 2, max = 2, message = "반드시 2개의 선택지를 등록해야 합니다.")
    val choices: List<CreateChoiceRequest>
)
