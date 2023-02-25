package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

class AlreadyMadeChoiceException : CategoryException(
    status = HttpStatus.BAD_REQUEST,
    errorCode = CategoryErrorCode.ALREADY_MADE_CHOICE,
    message = "이미 투표했습니다."
)
