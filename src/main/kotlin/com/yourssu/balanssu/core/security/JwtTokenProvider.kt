package com.yourssu.balanssu.core.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.DecodingException
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.Date
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    @Value("\${application.jwt.secret-key}") private val jwtSecret: String,
    @Value("\${application.jwt.refresh-token-valid-ms}") private val refreshTokenValidMilSecond: Long,
    @Value("\${application.jwt.access-token-valid-ms}") private val accessTokenValidMilSecond: Long
) {
    private val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

    fun generateRefreshToken(username: String, roles: Set<UserRole>) =
        generateToken(username, roles, refreshTokenValidMilSecond)

    fun generateAccessToken(username: String, roles: Set<UserRole>) =
        generateToken(username, roles, accessTokenValidMilSecond)

    private fun generateToken(username: String, roles: Set<UserRole>, tokenValidMillisecond: Long): JwtTokenDto {
        val now = Date()
        val expireIn = now.time + tokenValidMillisecond
        val token = Jwts.builder()
            .claim("id", username)
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(Date(expireIn))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()

        return JwtTokenDto(token, expireIn)
    }

    fun resolveToken(request: HttpServletRequest): Claims? {
        var token = request.getHeader("Authorization")
        token = when {
            token == null -> return null
            token.startsWith("Bearer ") -> token.replace("Bearer ", "")
            else -> throw DecodingException("")
        }
        return getClaims(token)
    }

    private fun getClaims(token: String) =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

    fun getAuthentication(claims: Claims) =
        UsernamePasswordAuthenticationToken(claims["id"], "", getAuthorities(claims))

    private fun getAuthorities(claims: Claims) =
        claims.get("roles", List::class.java)
            .map { SimpleGrantedAuthority(it as String) }

    fun getUsername(token: String) =
        getClaims(token)["id"] as String
}
