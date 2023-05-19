package com.yourssu.balanssu.application.exception

import com.yourssu.balanssu.core.response.ErrorResponse
import com.yourssu.balanssu.domain.exception.CannotDeleteCommentException
import com.yourssu.balanssu.domain.exception.CommentException
import com.yourssu.balanssu.domain.exception.CommentNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CommentExceptionHandler {
    @ExceptionHandler(CommentNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: CommentException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)

    @ExceptionHandler(CannotDeleteCommentException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflictException(exception: CommentException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)
}
