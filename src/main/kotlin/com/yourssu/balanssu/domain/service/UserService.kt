package com.yourssu.balanssu.domain.service

import com.yourssu.balanssu.core.security.JwtTokenProvider
import com.yourssu.balanssu.core.security.UserRole
import com.yourssu.balanssu.domain.exception.CannotRefreshTokenException
import com.yourssu.balanssu.domain.exception.CannotSignUpException
import com.yourssu.balanssu.domain.exception.NicknameInUseException
import com.yourssu.balanssu.domain.exception.PasswordNotMatchedException
import com.yourssu.balanssu.domain.exception.UserNotFoundException
import com.yourssu.balanssu.domain.exception.UsernameInUseException
import com.yourssu.balanssu.domain.model.dto.AuthTokenDto
import com.yourssu.balanssu.domain.model.dto.SignInDto
import com.yourssu.balanssu.domain.model.dto.SignUpDto
import com.yourssu.balanssu.domain.model.dto.UserInfoDto
import com.yourssu.balanssu.domain.model.entity.User
import com.yourssu.balanssu.domain.model.repository.UserRepository
import java.time.Clock
import java.time.LocalDateTime
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {
    fun signUp(dto: SignUpDto) {
        if (userRepository.existsByUsernameOrNickname(dto.username, dto.nickname)) {
            throw CannotSignUpException()
        }

        val user = User(
            username = dto.username,
            password = passwordEncoder.encode(dto.password),
            nickname = dto.nickname,
            schoolAge = dto.schoolAge,
            mbti = dto.mbti,
            gender = dto.gender
        )
        userRepository.save(user)
    }

    fun validateUsername(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw UsernameInUseException()
        }
    }

    fun validateNickname(nickname: String) {
        if (userRepository.existsByNickname(nickname)) {
            throw NicknameInUseException()
        }
    }

    fun signIn(dto: SignInDto): AuthTokenDto {
        val user = userRepository.findByUsernameAndIsDeletedIsFalse(dto.username) ?: throw UserNotFoundException()
        if (!passwordEncoder.matches(dto.password, user.password)) {
            throw PasswordNotMatchedException()
        }

        val refreshToken = jwtTokenProvider.generateRefreshToken(dto.username, setOf(user.role))
        val accessToken = jwtTokenProvider.generateAccessToken(dto.username, setOf(user.role))

        user.refreshToken = refreshToken.token

        return AuthTokenDto(refreshToken, accessToken)
    }

    fun getUserInfo(username: String): UserInfoDto {
        val user = userRepository.findByUsername(username)
        return UserInfoDto(user.username, user.nickname, user.mbti!!, user.schoolAge!!)
    }

    fun refreshToken(token: String): AuthTokenDto {
        val username = jwtTokenProvider.getUsername(token)
        val user = userRepository.findByUsernameAndRefreshToken(username, token) ?: throw CannotRefreshTokenException()

        val refreshToken = jwtTokenProvider.generateRefreshToken(username, setOf(UserRole.ROLE_USER))
        val accessToken = jwtTokenProvider.generateAccessToken(username, setOf(UserRole.ROLE_USER))

        user.renewRefreshToken(refreshToken.token)

        return AuthTokenDto(refreshToken, accessToken)
    }

    fun deleteUser(username: String) {
        val user = userRepository.findByUsername(username)
        user.delete()
    }
}
