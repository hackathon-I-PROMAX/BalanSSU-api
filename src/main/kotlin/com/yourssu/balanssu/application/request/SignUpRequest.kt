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
    @field:Size(min = 2, max = 2, message = "학번은 반드시 2자리를 입력해야 합니다.")
    val schoolAge: String,
    @field:Size(min = 4, max = 4, message = "MBTI는 반드시 4자리를 입력해야 합니다.")
    val mbti: String,
    @field:Size(min = 1, max = 1, message = "성별은 반드시 'M' 또는 'F' 중 하나를 입력해야 합니다.")
    val gender: String
) {
    companion object {
        const val PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[!-~₩]{8,100}$"
    }
}
