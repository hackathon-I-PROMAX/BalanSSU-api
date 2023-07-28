package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class RestrictedUserException : UserException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = UserErrorCode.RESTRICTED_USER,
    message = "이용이 제한된 사용자입니다."
)
