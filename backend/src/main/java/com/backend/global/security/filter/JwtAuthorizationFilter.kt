package com.backend.global.security.filter

import com.backend.domain.user.entity.SiteUser
import com.backend.global.exception.GlobalErrorCode
import com.backend.global.redis.repository.RedisRepository
import com.backend.global.response.GenericResponse
import com.backend.global.security.SecurityConfig
import com.backend.global.security.custom.CustomUserDetails
import com.backend.standard.util.AuthResponseUtil
import com.backend.standard.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.concurrent.TimeUnit

class JwtAuthorizationFilter(
    private val jwtUtil: JwtUtil,
    private val ACCESS_EXPIRATION: Long,
    private val REFRESH_EXPIRATION: Long,
    private val objectMapper: ObjectMapper,
    private val redisRepository: RedisRepository
) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val requestURI = request.requestURI

        if (requestURI == "/api/v1/reissue") {
            reissueFilter(request, response)
        } else {
            accessFilter(request, response, filterChain)
        }
    }

    @Throws(ServletException::class, IOException::class)
    private fun reissueFilter(req: HttpServletRequest, resp: HttpServletResponse) {
        val refreshToken = getRefreshToken(req)

        if (refreshToken == null) {
            AuthResponseUtil.failLogin(
                resp,
                GenericResponse.fail<Any>(),
                HttpServletResponse.SC_BAD_REQUEST,
                objectMapper
            )
            return
        }

        val username = jwtUtil.getUsername(refreshToken)
        val role = jwtUtil.getRole(refreshToken)

        if (redisRepository.get(username) != refreshToken) {
            AuthResponseUtil.failLogin(
                resp,
                GenericResponse.fail<Any>(),
                HttpServletResponse.SC_BAD_REQUEST,
                objectMapper
            )
            return
        }

        val userDetails = CustomUserDetails(
            SiteUser(jwtUtil.getUserId(refreshToken), username, role)
        )

        val newAccessToken = jwtUtil.createAccessToken(userDetails, ACCESS_EXPIRATION)
        val newRefreshToken = jwtUtil.createRefreshToken(userDetails, REFRESH_EXPIRATION)

        userDetails.username?.let { redisRepository.remove(it) }
        userDetails.username?.let {
            redisRepository.save(it, newRefreshToken, REFRESH_EXPIRATION, TimeUnit.MILLISECONDS)
        }

        AuthResponseUtil.success(
            resp,
            newAccessToken,
            jwtUtil.setJwtCookie("refreshToken", newRefreshToken, REFRESH_EXPIRATION),
            HttpServletResponse.SC_OK,
            GenericResponse.ok(userDetails.username, "AccessToken 재발급 성공"),
            objectMapper
        )
    }

    @Throws(ServletException::class, IOException::class)
    private fun accessFilter(req: HttpServletRequest, resp: HttpServletResponse, filterChain: FilterChain) {
        val authorization = req.getHeader("Authorization")

        if (isPublicUrl(req)) {
            filterChain.doFilter(req, resp)
            return
        }

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // Public URL이면 통과
            if (isPublicUrl(req)) {
                filterChain.doFilter(req, resp)
                return
            }

            AuthResponseUtil.failLogin(
                resp,
                GenericResponse.fail<Any>(GlobalErrorCode.UNAUTHENTICATION_USER),
                HttpServletResponse.SC_UNAUTHORIZED,
                objectMapper
            )
            return
        }

        val accessToken = authorization.substring(7)

        try {
            jwtUtil.isExpired(accessToken)
            val username = jwtUtil.getUsername(accessToken)
            val role = jwtUtil.getRole(accessToken)
            val userId = jwtUtil.getUserId(accessToken)

            val userDetails = CustomUserDetails(
                SiteUser(userId, username, role)
            )

            val authentication: Authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(req, resp)
        } catch (e: ExpiredJwtException) {
            AuthResponseUtil.failLogin(
                resp,
                GenericResponse.fail<Any>(GlobalErrorCode.UNAUTHENTICATION_USER),
                HttpServletResponse.SC_UNAUTHORIZED,
                objectMapper
            )
        } catch (e: JwtException) {
            AuthResponseUtil.failLogin(
                resp,
                GenericResponse.fail<Any>(GlobalErrorCode.INVALID_TOKEN),
                HttpServletResponse.SC_UNAUTHORIZED,
                objectMapper
            )
        }
    }

    private fun getRefreshToken(request: HttpServletRequest): String? {
        if (request.cookies == null) {
            return null
        }

        var refreshToken: String? = null

        for (cookie in request.cookies) {
            if (cookie.name == "refreshToken") {
                refreshToken = cookie.value
            }
        }

        return refreshToken
    }

    private fun isPublicUrl(request: HttpServletRequest): Boolean {
        val requestUri = request.requestURI
        val method = HttpMethod.valueOf(request.method)

        val patterns = SecurityConfig.getPublicUrls()[method] ?: return false

        return patterns.any { pattern ->
            AntPathMatcher().match(pattern, requestUri)
        }
    }
}