package com.yourssu.balanssu.application.exception

import com.yourssu.balanssu.core.response.ErrorResponse
import com.yourssu.balanssu.domain.exception.CannotCreateCategoryException
import com.yourssu.balanssu.domain.exception.CategoryException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CategoryExceptionHandler {
    @ExceptionHandler(CannotCreateCategoryException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(exception: CategoryException) =
        ErrorResponse(exception.status, exception.errorCode, exception.message!!)
}
