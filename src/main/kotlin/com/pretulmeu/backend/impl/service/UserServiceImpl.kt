package com.pretulmeu.backend.impl.service

import com.pretulmeu.backend.api.dto.AuthenticationResponseDto
import com.pretulmeu.backend.api.dto.UserAuthenticationDto
import com.pretulmeu.backend.api.dto.UserDto
import com.pretulmeu.backend.api.service.SecurityService
import com.pretulmeu.backend.api.service.UserService
import com.pretulmeu.backend.impl.exceptions.PasswordTooShortException
import com.pretulmeu.backend.impl.exceptions.UserAlreadyExists
import com.pretulmeu.backend.impl.exceptions.UsernameIncorrectFormat
import com.pretulmeu.backend.impl.model.User
import com.pretulmeu.backend.impl.repo.UserRepo
import com.pretulmeu.backend.impl.validators.PasswordValidator
import com.pretulmeu.backend.impl.validators.UsernameValidator
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class UserServiceImpl(
    val userRepo: UserRepo,
    val passwordEncoder: PasswordEncoder,
    val securityService: SecurityService,
    val passwordValidator: PasswordValidator,
    val usernameValidator: UsernameValidator
): UserService {
    override fun login(dto: UserAuthenticationDto): AuthenticationResponseDto {

        securityService.authenticate(dto.username, dto.password)
        val tok = securityService.generateToken(dto.username, dto.password)
        return AuthenticationResponseDto("Bearer ${tok}")
    }

    override fun register(dto: UserAuthenticationDto): AuthenticationResponseDto {
        usernameValidator.validate(dto.username)?.let { throw UsernameIncorrectFormat("Username is not an email") }
        userRepo.findByUsername(dto.username)?.let { throw UserAlreadyExists("User with given username already exists") }
        passwordValidator.validate(dto.password)?.let { throw PasswordTooShortException(it) }
        userRepo.save(User(username = dto.username, password = passwordEncoder.encode(dto.password)))
        securityService.authenticate(dto.username, dto.password)

        val tok = securityService.generateToken(dto.username, dto.password)
        return AuthenticationResponseDto("Bearer ${tok}")
    }

    override fun getAll(): String {
        return securityService.user?.username ?: "Error"
    }

    override fun getLoggedUser(): UserDto? {
        return securityService.user
    }
}