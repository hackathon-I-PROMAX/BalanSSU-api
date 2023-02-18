package com.yourssu.balanssu.core.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.yourssu.balanssu.core.response.ErrorResponse
import org.hibernate.exception.ConstraintViolationException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GeneralExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestBodyException(exception: HttpMessageNotReadableException): ErrorResponse {
        val message = when (val cause = exception.cause) {
            is MissingKotlinParameterException -> {
                "${cause.parameter.name}이 누락되었습니다."
            }

            is InvalidFormatException -> {
                "${cause.path[0].fieldName}이 잘못 입력되었습니다."
            }

            else -> {
                "잘못된 request body입니다."
            }
        }

        return ErrorResponse(HttpStatus.BAD_REQUEST, "SYSTEM-001", message)
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMissingServletRequestParameterException(exception: MissingServletRequestParameterException) =
        ErrorResponse(HttpStatus.BAD_REQUEST, "SYSTEM-002", "${exception.parameterName}이 누락되었습니다.")

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentTypeMismatch(exception: MethodArgumentTypeMismatchException) =
        ErrorResponse(HttpStatus.BAD_REQUEST, "SYSTEM-003", "${exception.name}이 잘못 입력되었습니다.")

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleRequestValid(exception: ConstraintViolationException) =
        ErrorResponse(HttpStatus.BAD_REQUEST, "SYSTEM-004", "잘못된 데이터 요청입니다.")

    @ExceptionHandler(HttpMediaTypeNotSupportedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMediaTypeNotSupportedException(exception: HttpMediaTypeNotSupportedException) =
        ErrorResponse(HttpStatus.BAD_REQUEST, "SYSTEM-005", "지원하지 않는 MediaType 입니다. 요청된 type: ${exception.contentType}")

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleHttpMethodNotSupportedException(exception: HttpRequestMethodNotSupportedException) =
        ErrorResponse(HttpStatus.BAD_REQUEST, "SYSTEM-006", "잘못된 Mapping 요청입니다.")
}
