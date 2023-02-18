package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class PasswordNotMatchedException : UserException(
    status = HttpStatus.CONFLICT,
    errorCode = UserErrorCode.PASSWORD_NOT_MATCHED,
    message = "비밀번호가 일치하지 않습니다."
)
