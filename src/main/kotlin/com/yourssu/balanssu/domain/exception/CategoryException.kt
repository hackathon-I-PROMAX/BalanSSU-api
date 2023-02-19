package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

abstract class CategoryException(
    val status: HttpStatus,
    errorCode: CategoryErrorCode,
    message: String
) : RuntimeException(message) {
    val errorCode = String.format("CATEGORY-%03d", errorCode.ordinal + 1)
}
