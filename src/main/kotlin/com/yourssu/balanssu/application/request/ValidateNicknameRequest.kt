package com.yourssu.balanssu.application.request

import javax.validation.constraints.Size

class ValidateNicknameRequest(
    @field:Size(min = 3, max = 64, message = "3자리 이상, 64자리 이하의 닉네임을 입력해야 합니다.")
    val nickname: String
)
