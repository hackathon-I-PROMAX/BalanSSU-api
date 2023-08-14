package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.ReportErrorCode
import org.springframework.http.HttpStatus

open class ReportException(
    val status: HttpStatus,
    errorCode: ReportErrorCode,
    message: String
) : RuntimeException(message) {
    val errorCode = String.format("REPORT-%03d", errorCode.ordinal + 1)
}
