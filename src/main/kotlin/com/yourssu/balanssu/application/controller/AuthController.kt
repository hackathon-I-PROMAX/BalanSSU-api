package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.RefreshRequest
import com.yourssu.balanssu.application.request.SignInRequest
import com.yourssu.balanssu.application.request.SignUpRequest
import com.yourssu.balanssu.application.response.TokenResponse
import com.yourssu.balanssu.domain.model.dto.SignInDto
import com.yourssu.balanssu.domain.model.dto.SignUpDto
import com.yourssu.balanssu.domain.service.UserService
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(private val userService: UserService) {
    @ApiOperation("회원가입")
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody request: SignUpRequest) =
        userService.signUp(SignUpDto(request.username, request.password))

    @ApiOperation("로그인")
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.CREATED)
    fun signIn(@RequestBody request: SignInRequest): TokenResponse {
        val token = userService.signIn(SignInDto(request.username, request.password))
        return TokenResponse(token)
    }

    @ApiOperation("토큰 재발급")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    fun refreshToken(@RequestBody request: RefreshRequest): TokenResponse {
        val token = userService.refreshToken(request.refreshToken)
        return TokenResponse(token)
    }
}
