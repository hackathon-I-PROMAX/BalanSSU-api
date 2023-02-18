package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

abstract class UserException(
    val status: HttpStatus,
    errorCode: UserErrorCode,
    message: String
) : RuntimeException(message) {
    val errorCode = String.format("USER-%03d", errorCode.ordinal + 1)
}
