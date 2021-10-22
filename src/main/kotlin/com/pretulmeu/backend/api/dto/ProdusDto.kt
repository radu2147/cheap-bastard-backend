package com.pretulmeu.backend.api.dto

data class ProdusDto(
    var id: Long = -1,
    val name: String,
    val img_name: String?,
    val history: Set<ShopProductDto>?,
    val lowestPrice: Float? = null
)