package com.yourssu.balanssu.core.response

import org.springframework.http.HttpStatus
import java.time.Clock
import java.time.LocalDateTime

class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String
) {
    constructor(httpStatus: HttpStatus, errorCode: String, message: String) : this(
        timestamp = LocalDateTime.now(Clock.systemDefaultZone()),
        status = httpStatus.value(),
        error = errorCode,
        message = message
    )
}
