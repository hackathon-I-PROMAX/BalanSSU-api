package com.yourssu.balanssu.application.request

import javax.validation.constraints.Size

class ValidateUsernameRequest(
    @field:Size(min = 5, max = 64, message = "5자리 이상, 64자리 이하의 아이디를 입력해야 합니다.")
    val username: String
)
