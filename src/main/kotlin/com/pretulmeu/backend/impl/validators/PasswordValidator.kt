package com.pretulmeu.backend.impl.validators

import org.springframework.stereotype.Component

@Component
class PasswordValidator {
    fun validate(password: String): String?{
        var message: String? = null
        if(password.length < 7){
            message = "Password should be at least 6 characters"
        }
        return message
    }
}