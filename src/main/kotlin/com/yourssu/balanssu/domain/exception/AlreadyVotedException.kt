package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.CategoryErrorCode
import org.springframework.http.HttpStatus

class AlreadyVotedException : CategoryException(
    status = HttpStatus.CONFLICT,
    errorCode = CategoryErrorCode.ALREADY_VOTED,
    message = "이미 투표했습니다."
)
