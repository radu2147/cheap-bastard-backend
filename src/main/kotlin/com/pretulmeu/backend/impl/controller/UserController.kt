package com.pretulmeu.backend.impl.controller

import com.pretulmeu.backend.api.UserApi
import com.pretulmeu.backend.api.dto.UserAuthenticationDto
import com.pretulmeu.backend.api.dto.AuthenticationResponseDto
import com.pretulmeu.backend.api.dto.UserDto
import com.pretulmeu.backend.api.service.UserService
import com.pretulmeu.backend.impl.exceptions.CustomException
import com.pretulmeu.backend.impl.exceptions.UserAlreadyExists
import com.pretulmeu.backend.impl.exceptions.UserDoesNotExist
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    val userService: UserService
): UserApi {
    override fun login(dto: UserAuthenticationDto): AuthenticationResponseDto = userService.login(dto)
    override fun register(dto: UserAuthenticationDto): AuthenticationResponseDto = userService.register(dto)

    override fun test(): String {
        return userService.getAll()
    }

    override fun getUser(): UserDto? = userService.getLoggedUser()

    @ExceptionHandler(value=[CustomException::class])
    fun userDoesNotExistHandler(err: CustomException): ResponseEntity<String>{
        return ResponseEntity.status(403).body(err.message)
    }
}