package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

class CategoryAlreadyExistsException : CategoryException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = CategoryErrorCode.ALREADY_EXISTS,
    message = "해당 질문은 이미 존재합니다."
)
