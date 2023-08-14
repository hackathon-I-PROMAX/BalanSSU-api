package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.ReportErrorCode
import org.springframework.http.HttpStatus

class CommentAlreadyReportedException : ReportException(
    status = HttpStatus.CONFLICT,
    errorCode = ReportErrorCode.ALREADY_REPORTED,
    message = "이미 신고한 댓글입니다."
)
