package com.pretulmeu.backend.api.service

import com.pretulmeu.backend.api.dto.UserAuthenticationDto
import com.pretulmeu.backend.api.dto.AuthenticationResponseDto
import com.pretulmeu.backend.api.dto.UserDto

interface UserService {
    fun login(dto: UserAuthenticationDto): AuthenticationResponseDto
    fun register(dto: UserAuthenticationDto): AuthenticationResponseDto
    fun getAll(): String
    fun getLoggedUser(): UserDto?
}