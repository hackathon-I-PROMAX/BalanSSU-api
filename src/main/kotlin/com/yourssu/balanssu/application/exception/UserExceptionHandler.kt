package com.yourssu.balanssu.application.exception

import com.yourssu.balanssu.core.response.ErrorResponse
import com.yourssu.balanssu.domain.exception.CannotRefreshTokenException
import com.yourssu.balanssu.domain.exception.JwtException
import com.yourssu.balanssu.domain.exception.PasswordNotMatchedException
import com.yourssu.balanssu.domain.exception.UserAlreadyExistsException
import com.yourssu.balanssu.domain.exception.UserException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {
    @ExceptionHandler(UserNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: UserException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)

    @ExceptionHandler(UserAlreadyExistsException::class, PasswordNotMatchedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflictException(exception: UserException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)

    @ExceptionHandler(CannotRefreshTokenException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(exception: JwtException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)
}
