package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.JwtErrorCode
import org.springframework.http.HttpStatus

open class JwtException(
    val status: HttpStatus,
    errorCode: JwtErrorCode,
    message: String
) : RuntimeException(message) {
    val errorCode = String.format("AUTH-%03d", errorCode.ordinal + 1)
}
