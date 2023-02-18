package com.yourssu.balanssu.application.controller

import com.yourssu.balanssu.core.security.Authenticated
import com.yourssu.balanssu.core.security.UserInfo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
    @PostMapping("/hello")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Authenticated user: UserInfo) {
        println("username: ${user.username}")
    }
}
