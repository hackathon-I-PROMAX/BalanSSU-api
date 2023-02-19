package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class NicknameInUseException : UserException(
    HttpStatus.BAD_REQUEST,
    UserErrorCode.USERNAME_IN_USE,
    "이미 사용중인 닉네임입니다."
)
