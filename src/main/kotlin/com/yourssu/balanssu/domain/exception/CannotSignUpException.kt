package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class CannotSignUpException : UserException(
    status = HttpStatus.CONFLICT,
    errorCode = UserErrorCode.CANNOT_SIGN_UP,
    message = "회원가입할 수 없습니다."
)
