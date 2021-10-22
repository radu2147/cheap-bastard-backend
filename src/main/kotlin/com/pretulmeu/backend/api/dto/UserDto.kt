package com.pretulmeu.backend.api.dto

data class UserDto(
    val username: String,
    val id: Long? = null,
    val produse: List<ProdusDto>? = null
)