package com.pretulmeu.backend.impl.controller

import com.pretulmeu.backend.api.ProdusApi
import com.pretulmeu.backend.api.dto.ProdusDto
import com.pretulmeu.backend.api.dto.ShopProductDto
import com.pretulmeu.backend.api.service.ProdusService
import com.pretulmeu.backend.impl.exceptions.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProdusController(val produsService: ProdusService): ProdusApi {
    override fun getProductsOfUser(): List<ProdusDto> = produsService.getProductOfUser()
    override fun getShopProduct(id: Long): ShopProductDto = produsService.getShopProduct(id)

    override fun getProductById(id: Long): ProdusDto = produsService.getProductById(id)

    override fun saveProduct(@RequestBody url: String) = produsService.saveProduct(url)
    override fun deleteProduct(id: Long) = produsService.deleteProduct(id)
    override fun deleteLink(id: Long) = produsService.deleteLink(id)

    override fun updateProduct(produsDto: ProdusDto) = produsService.updateProduct(produsDto)
    override fun addLink(id: Long, url: String) = produsService.addLink(id, url)
    @ExceptionHandler(value=[CustomException::class])
    fun productExistHandler(err: CustomException): ResponseEntity<String> {
        return ResponseEntity.status(404).body(err.message)
    }
}