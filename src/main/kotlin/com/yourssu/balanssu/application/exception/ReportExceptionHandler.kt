package com.yourssu.balanssu.application.exception

import com.yourssu.balanssu.core.response.ErrorResponse
import com.yourssu.balanssu.domain.exception.CannotReportOwnCommentException
import com.yourssu.balanssu.domain.exception.CommentAlreadyReportedException
import com.yourssu.balanssu.domain.exception.ReportException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ReportExceptionHandler {
    @ExceptionHandler(CannotReportOwnCommentException::class, CommentAlreadyReportedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflictException(exception: ReportException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)
}
