package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

class CategoryNotFoundException : CategoryException(
    status = HttpStatus.NOT_FOUND,
    errorCode = CategoryErrorCode.NOT_FOUND,
    message = "카테고리가 존재하지 않습니다."
)
