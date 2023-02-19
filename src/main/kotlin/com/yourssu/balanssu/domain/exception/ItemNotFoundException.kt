package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

class ItemNotFoundException : CategoryException(
    status = HttpStatus.NOT_FOUND,
    errorCode = CategoryErrorCode.NOT_FOUND,
    message = "선택지가 없습니다."
)
