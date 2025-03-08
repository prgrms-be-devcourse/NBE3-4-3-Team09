package com.backend.global.security.filter

import com.backend.domain.user.dto.request.LoginRequest
import com.backend.domain.user.dto.response.LoginResponse
import com.backend.global.exception.GlobalErrorCode
import com.backend.global.redis.repository.RedisRepository
import com.backend.global.response.GenericResponse
import com.backend.global.response.GenericResponse.Companion.ok
import com.backend.global.security.custom.CustomUserDetails
import com.backend.standard.util.AuthResponseUtil
import com.backend.standard.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.util.concurrent.TimeUnit

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val ACCESS_EXPIRATION: Long,
    private val REFRESH_EXPIRATION: Long,
    private val objectMapper: ObjectMapper,
    private val redisRepository: RedisRepository,
    private val authenticationManager: AuthenticationManager
) : UsernamePasswordAuthenticationFilter() {
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(req: HttpServletRequest, resp: HttpServletResponse?): Authentication? {
        try {
            val loginRequest = objectMapper.readValue<LoginRequest>(req.inputStream, LoginRequest::class.java)

            val email = loginRequest.email
            val password = loginRequest.password

            val authToken = UsernamePasswordAuthenticationToken(email, password)

            return authenticationManager.authenticate(authToken)
        } catch (e: IOException) {
            throw AuthenticationServiceException("잘못된 로그인 정보입니다.")
        }
    }

    @Throws(IOException::class)
    override fun unsuccessfulAuthentication(
        req: HttpServletRequest?,
        resp: HttpServletResponse,
        failed: AuthenticationException?
    ) {
        AuthResponseUtil.failLogin(
            resp,
            GenericResponse.fail<Any>(GlobalErrorCode.UNAUTHORIZATION_USER),
            HttpServletResponse.SC_UNAUTHORIZED,
            objectMapper
        )
    }

    @Throws(IOException::class)
    override fun successfulAuthentication(
        req: HttpServletRequest?,
        resp: HttpServletResponse,
        chain: FilterChain?,
        authentication: Authentication
    ) {
        val userDetails = authentication.principal as CustomUserDetails

        val accessToken = jwtUtil.createAccessToken(userDetails, ACCESS_EXPIRATION)
        val refreshToken = jwtUtil.createRefreshToken(userDetails, REFRESH_EXPIRATION)

        userDetails.username?.let { redisRepository.remove(it) }
        userDetails.username?.let {
            redisRepository.save(it, refreshToken, REFRESH_EXPIRATION, TimeUnit.MILLISECONDS)
        }

        AuthResponseUtil.success(
            resp,
            accessToken,
            jwtUtil.setJwtCookie("refreshToken", refreshToken, REFRESH_EXPIRATION),
            HttpServletResponse.SC_OK,
            ok<LoginResponse?>(LoginResponse(userDetails.username!!), "로그인이 성공적으로 이루어졌습니다."),
            objectMapper
        )
    }
}
