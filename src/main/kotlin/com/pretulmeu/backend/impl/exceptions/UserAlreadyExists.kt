package com.pretulmeu.backend.impl.exceptions

class UserAlreadyExists(override val message: String): CustomException(message) {
}