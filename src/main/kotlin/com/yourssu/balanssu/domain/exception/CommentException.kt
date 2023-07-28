package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CommentErrorCode
import org.springframework.http.HttpStatus

abstract class CommentException(
    val status: HttpStatus,
    errorCode: CommentErrorCode,
    message: String
) : RuntimeException(message) {
    val errorCode = String.format("COMMENT-%03d", errorCode.ordinal + 1)
}
