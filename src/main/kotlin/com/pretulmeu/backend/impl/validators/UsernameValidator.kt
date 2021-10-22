package com.pretulmeu.backend.impl.validators

import org.springframework.stereotype.Component

@Component
class UsernameValidator {
    fun validate(username: String): String?{
        var message: String? = null
        if(!username.matches(Regex(".+[@].+"))){
            message = "Username should be email format"
        }
        return message
    }
}