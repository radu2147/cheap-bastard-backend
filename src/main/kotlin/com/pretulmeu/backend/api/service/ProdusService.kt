package com.pretulmeu.backend.api.service

import com.pretulmeu.backend.api.dto.ProdusDto
import com.pretulmeu.backend.api.dto.ShopProductDto

interface ProdusService {
    fun getProductOfUser(): List<ProdusDto>
    fun saveProduct(url: String)
    fun deleteProduct(id: Long)
    fun updateProduct(produsDto: ProdusDto)
    fun getProductById(id: Long): ProdusDto
    fun deleteLink(id: Long)
    fun addLink(id: Long, url: String)
    fun getShopProduct(id: Long): ShopProductDto
}
