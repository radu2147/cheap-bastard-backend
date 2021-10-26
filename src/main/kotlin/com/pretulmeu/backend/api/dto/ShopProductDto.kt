package com.pretulmeu.backend.api.dto

import com.pretulmeu.backend.impl.providers.ShopProvider

data class ShopProductDto(
    val id: Long = -1,
    val name: String,
    val link: String?,
    val history: Set<PricePerDateDto>?,
    val currentPrice: Float? = null,
    val provider: String? = link?.let{ ShopProvider.getProviderName(it) },
)
