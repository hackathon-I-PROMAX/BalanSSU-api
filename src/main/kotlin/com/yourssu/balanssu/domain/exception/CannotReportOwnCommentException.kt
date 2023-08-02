package com.yourssu.balanssu.domain.exception

import com.yourssu.balanssu.domain.errorCode.ReportErrorCode
import org.springframework.http.HttpStatus

class CannotReportOwnCommentException : ReportException(
    status = HttpStatus.CONFLICT,
    errorCode = ReportErrorCode.CANNOT_REPORT_OWN_COMMENT,
    message = "자신이 작성한 댓글은 신고할 수 없습니다."
)
