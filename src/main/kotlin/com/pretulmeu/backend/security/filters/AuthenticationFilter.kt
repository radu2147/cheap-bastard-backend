package com.pretulmeu.backend.security.filters

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.pretulmeu.backend.utils.Constants
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthorizationFilter(val userDetailsService: UserDetailsService): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(header == null || header == "" || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response)
            return
        }
        val token = header.split("Bearer ")[1]
        lateinit var decodedToken: DecodedJWT
        try{
            val algorithm: Algorithm = Algorithm.HMAC256(Constants.JWT_SECRET)
            val verifier = JWT.require(algorithm).build()
            decodedToken = verifier.verify(token)
        }
        catch (e: Exception){
            response.status = 403
            response.writer.write("Your token expired. Please login again.")
            filterChain.doFilter(request, response)
            return
        }

        val details = userDetailsService.loadUserByUsername(decodedToken.subject)
        val authentication = UsernamePasswordAuthenticationToken(details, null, null)

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)

    }
}