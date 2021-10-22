package com.pretulmeu.backend.impl.exceptions

abstract class CustomException(override val message: String): Exception(message) {
}