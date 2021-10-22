package com.pretulmeu.backend.api.service

import com.pretulmeu.backend.api.dto.UserDto
import com.pretulmeu.backend.impl.model.User

internal interface SecurityService {
    val user: UserDto?

    fun authenticate(username: String, password: String)

    fun generateToken(username: String, password: String): String

    fun checkSameUser(user: User): Boolean
}