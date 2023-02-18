package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.UserErrorCode
import org.springframework.http.HttpStatus

class UserNotFoundException : UserException(
    status = HttpStatus.NOT_FOUND,
    errorCode = UserErrorCode.NOT_FOUND,
    message = "일치하는 아이디가 없습니다."
)
