package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class UsernameInUseException : UserException(
    HttpStatus.BAD_REQUEST,
    UserErrorCode.USERNAME_IN_USE,
    "이미 사용중인 아이디입니다."
)
