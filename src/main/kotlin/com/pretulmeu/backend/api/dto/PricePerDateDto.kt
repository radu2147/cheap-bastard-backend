package com.pretulmeu.backend.api.dto

import java.sql.Date

data class PricePerDateDto(
    val price: Float,
    val date: Date
)