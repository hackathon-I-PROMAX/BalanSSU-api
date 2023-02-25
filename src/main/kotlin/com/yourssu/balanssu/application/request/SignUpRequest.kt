package com.yourssu.balanssu.application.request

import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class SignUpRequest(
    @field:Size(min = 3, max = 64, message = "3자리 이상, 64자리 이하의 아이디를 입력해야 합니다.")
    val username: String,
    @field:Pattern(regexp = PASSWORD_REGEX, message = "유효하지 않은 비밀번호입니다.")
    val password: String,
    @field:Size(min = 3, max = 64, message = "3자리 이상, 64자리 이하의 닉네임을 입력해야 합니다.")
    val nickname: String,
    val schoolAge: String,
    val departure: String,
    val gender: Char
) {
    companion object {
        const val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[!-~₩]{8,100}$"
    }
}
