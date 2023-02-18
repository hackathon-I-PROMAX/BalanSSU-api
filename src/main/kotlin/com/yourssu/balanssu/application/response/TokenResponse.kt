package com.yourssu.balanssu.application.response

import com.yourssu.balanssu.domain.model.dto.AuthTokenDto

class TokenResponse(
    val refreshToken: String?,
    val refreshTokenExpiresIn: Long?,
    val accessToken: String,
    val accessTokenExpiresIn: Long,
) {
    constructor(token: AuthTokenDto) : this(
        token.refreshToken?.token,
        token.refreshToken?.expiresIn,
        token.accessToken.token,
        token.accessToken.expiresIn
    )
}
