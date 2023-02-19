package com.yourssu.balanssu.domain.model.dto

import com.yourssu.balanssu.core.security.JwtTokenDto

class AuthTokenDto(
    val refreshToken: JwtTokenDto,
    val accessToken: JwtTokenDto
)
