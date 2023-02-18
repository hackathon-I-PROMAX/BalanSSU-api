package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.JwtErrorCode
import org.springframework.http.HttpStatus

class CannotRefreshTokenException : JwtException(
    status = HttpStatus.UNAUTHORIZED,
    errorCode = JwtErrorCode.CANNOT_REFRESH,
    message = "토큰을 발급할 수 없습니다."
)
