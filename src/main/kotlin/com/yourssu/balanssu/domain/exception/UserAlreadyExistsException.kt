package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class UserAlreadyExistsException(username: String) : UserException(
    status = HttpStatus.CONFLICT,
    errorCode = UserErrorCode.ALREADY_EXISTS,
    message = "아이디 '$username'는 이미 존재합니다."
)
