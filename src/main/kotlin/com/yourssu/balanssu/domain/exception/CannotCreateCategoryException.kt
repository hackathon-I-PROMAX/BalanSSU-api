package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

class CannotCreateCategoryException : CategoryException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = CategoryErrorCode.CANNOT_CREATE,
    message = "질문을 생성할 수 없습니다."
)
