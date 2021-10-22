package com.pretulmeu.backend.api

import com.pretulmeu.backend.api.dto.ProdusDto
import com.pretulmeu.backend.api.dto.ShopProductDto
import com.pretulmeu.backend.utils.Constants
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("${Constants.BASE_PATH}/products")
interface ProdusApi {

    @CrossOrigin(origins = ["*"])
    @GetMapping(path = [""])
    fun getProductsOfUser(): List<ProdusDto>

    @CrossOrigin(origins = ["*"])
    @GetMapping(path = ["/shopProduct/{id}"])
    fun getShopProduct(@PathVariable id: Long): ShopProductDto

    @CrossOrigin(origins = ["*"])
    @GetMapping(path = ["/{id}"])
    fun getProductById(@PathVariable id: Long): ProdusDto

    @CrossOrigin(origins = ["*"])
    @PostMapping(path = [""])
    fun saveProduct(@RequestBody url: String)

    @CrossOrigin(origins = ["*"])
    @DeleteMapping(path = ["/{id}"])
    fun deleteProduct(@PathVariable id: Long)

    @CrossOrigin(origins = ["*"])
    @DeleteMapping(path = ["/links/{id}"])
    fun deleteLink(@PathVariable id: Long)

    @CrossOrigin(origins = ["*"])
    @PutMapping(path = [""])
    fun updateProduct(@RequestBody produsDto: ProdusDto)

    @CrossOrigin(origins = ["*"])
    @PostMapping(path = ["/{id}"])
    fun addLink(@PathVariable id: Long, @RequestBody url: String)

}