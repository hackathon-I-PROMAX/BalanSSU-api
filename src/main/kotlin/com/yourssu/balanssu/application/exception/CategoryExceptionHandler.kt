package com.yourssu.balanssu.application.exception

import com.yourssu.balanssu.core.response.ErrorResponse
import com.yourssu.balanssu.domain.exception.AlreadyMadeChoiceException
import com.yourssu.balanssu.domain.exception.CategoryAlreadyExistsException
import com.yourssu.balanssu.domain.exception.CategoryException
import com.yourssu.balanssu.domain.exception.CategoryNotFoundException
import com.yourssu.balanssu.domain.exception.ChoiceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CategoryExceptionHandler {
    @ExceptionHandler(CategoryAlreadyExistsException::class, AlreadyMadeChoiceException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(exception: CategoryException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)

    @ExceptionHandler(CategoryNotFoundException::class, ChoiceNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: CategoryException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)
}
