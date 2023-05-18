package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.application.request.RefreshRequest
import com.yourssu.balanssu.application.request.SignInRequest
import com.yourssu.balanssu.application.request.SignUpRequest
import com.yourssu.balanssu.application.request.ValidateNicknameRequest
import com.yourssu.balanssu.application.request.ValidateUsernameRequest
import com.yourssu.balanssu.application.response.TokenResponse
import com.yourssu.balanssu.application.response.ViewUserInfoResponse
import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
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
import javax.validation.Valid
import org.springframework.web.bind.annotation.GetMapping

@RestController
@RequestMapping("/auth")
class AuthController(private val userService: UserService) {
    @ApiOperation("회원가입")
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody @Valid request: SignUpRequest) =
        userService.signUp(
            SignUpDto(
                username = request.username,
                password = request.password,
                nickname = request.nickname,
                schoolAge = request.schoolAge,
                departure = request.departure,
                gender = request.gender[0]
            )
        )

    @ApiOperation("아이디 유효성 검사")
    @PostMapping("/validation/username")
    @ResponseStatus(HttpStatus.OK)
    fun validateUsername(@RequestBody @Valid request: ValidateUsernameRequest) =
        userService.validateUsername(request.username)

    @ApiOperation("닉네임 유효성 검사")
    @PostMapping("/validation/nickname")
    @ResponseStatus(HttpStatus.OK)
    fun validateNickname(@RequestBody @Valid request: ValidateNicknameRequest) =
        userService.validateNickname(request.nickname)

    @ApiOperation("로그인")
    @PostMapping("/sign-in")
    @ResponseStatus(HttpStatus.OK)
    fun signIn(@RequestBody request: SignInRequest): TokenResponse {
        val token = userService.signIn(SignInDto(request.username, request.password))
        return TokenResponse(token)
    }

    @ApiOperation("사용자 정보 조회")
    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    fun viewUserInfo(@Authenticated userInfo: UserInfo): ViewUserInfoResponse {
        val user = userService.getUserInfo(userInfo.username)
        return ViewUserInfoResponse(user)
    }

    @ApiOperation("토큰 재발급")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    fun refreshToken(@RequestBody request: RefreshRequest): TokenResponse {
        val token = userService.refreshToken(request.refreshToken)
        return TokenResponse(token)
    }
}
