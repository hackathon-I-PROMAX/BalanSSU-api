package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.security.JwtTokenProvider
import com.yourssu.balanssu.core.security.UserRole
import com.yourssu.balanssu.domain.exception.CannotRefreshTokenException
import com.yourssu.balanssu.domain.exception.PasswordNotMatchedException
import com.yourssu.balanssu.domain.exception.UserAlreadyExistsException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import com.yourssu.balanssu.domain.model.dto.AuthTokenDto
import com.yourssu.balanssu.domain.model.dto.SignInDto
import com.yourssu.balanssu.domain.model.dto.SignUpDto
import com.yourssu.balanssu.domain.model.entity.User
import com.yourssu.balanssu.domain.model.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun signUp(dto: SignUpDto) {
        if (userRepository.existsByUsername(dto.username)) {
            throw UserAlreadyExistsException(dto.username)
        }
        userRepository.save(User(dto.username, dto.password))
    }

    fun signIn(dto: SignInDto): AuthTokenDto {
        val user = userRepository.findByUsername(dto.username) ?: throw UserNotFoundException()
        if (user.password != dto.password) {
            throw PasswordNotMatchedException()
        }

        val refreshToken = jwtTokenProvider.generateRefreshToken(dto.username, setOf(UserRole.ROLE_USER))
        val accessToken = jwtTokenProvider.generateAccessToken(dto.username, setOf(UserRole.ROLE_USER))

        user.refreshToken = refreshToken.token

        return AuthTokenDto(refreshToken, accessToken)
    }

    fun refreshToken(refreshToken: String): AuthTokenDto {
        val username = jwtTokenProvider.getUsername(refreshToken)
        if (!userRepository.existsByUsernameAndRefreshToken(username, refreshToken)) {
            throw CannotRefreshTokenException()
        }

        val accessToken = jwtTokenProvider.generateAccessToken(username, setOf(UserRole.ROLE_USER))
        return AuthTokenDto(null, accessToken)
    }
}
