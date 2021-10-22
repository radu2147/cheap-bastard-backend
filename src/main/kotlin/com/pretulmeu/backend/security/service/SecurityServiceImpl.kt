package com.pretulmeu.backend.security.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.pretulmeu.backend.api.dto.UserDto
import com.pretulmeu.backend.api.service.SecurityService
import com.pretulmeu.backend.impl.exceptions.UserDoesNotExist
import com.pretulmeu.backend.impl.model.User
import com.pretulmeu.backend.utils.Constants
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
internal class SecurityServiceImpl(
    val authManager: AuthenticationManager,
): SecurityService{
    override val user: UserDto?
        get() = SecurityContextHolder.getContext().authentication.principal?.let { UserDto((it as UserDetails).username) }


    override fun authenticate(username: String, password: String) {
        try {
            val token = UsernamePasswordAuthenticationToken(username, password)
            authManager.authenticate(token)
        }
        catch (err: AuthenticationException){
            throw UserDoesNotExist("Combination of username and password does not exist")
        }
    }

    override fun generateToken(username: String, password: String): String {
        return JWT.create()
            .withClaim("password", password)
            .withSubject(username)
            .withExpiresAt(Date(Date().time + 172_800_000))
            .sign(Algorithm.HMAC256(Constants.JWT_SECRET))
    }

    override fun checkSameUser(user: User): Boolean {
        return user.username == this.user?.username
    }
}