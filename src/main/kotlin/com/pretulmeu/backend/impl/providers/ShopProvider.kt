package com.pretulmeu.backend.impl.providers

import com.pretulmeu.backend.api.dto.PricePerDateDto
import com.pretulmeu.backend.api.dto.ProdusDto
import com.pretulmeu.backend.api.dto.ShopProductDto
import com.pretulmeu.backend.impl.exceptions.InvalidSelectorException
import com.pretulmeu.backend.impl.exceptions.NoExistingProviderException
import com.pretulmeu.backend.impl.exceptions.UrlTooLongException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.sql.Date
import java.util.*

abstract class ShopProvider {

    abstract val name: String

    abstract protected fun getImage(doc: Document): String

    abstract protected fun getProductNameTag(doc: Document): String

    abstract protected fun getPrice(doc: Document): Float


    fun getProdusDto(url: String): ProdusDto{
        if(url.length >= 500){
            throw UrlTooLongException("Url is too long. Try another product")
        }
        val conn = Jsoup.connect(url)
        conn.userAgent("Mozilla/5.0");

        //set timeout to 10 seconds
        conn.timeout(10 * 1000);
        val doc: Document = conn.get()
        val productName = getProductNameTag(doc)
        val productPic = getImage(doc)
        val productPrice = getPrice(doc)

        return ProdusDto(
            -1,
            productName,
            productPic,
            setOf(
                ShopProductDto(
                -1,
                productName,
                url,
                setOf(PricePerDateDto(
                    productPrice,
                    Date(Calendar.getInstance().time.time)
                ))
            )
            )
        )
    }

    fun getShopProductDto(url: String): ShopProductDto{
        if(url.length >= 500){
            throw UrlTooLongException("Url is too long. Try another product")
        }
        val doc: Document = Jsoup.connect(url).get()
        val productName = getProductNameTag(doc)
        val productPrice = getPrice(doc)
        return ShopProductDto(
                    -1,
                    productName,
                    url,
                    setOf(PricePerDateDto(
                        productPrice,
                        Date(Calendar.getInstance().time.time)
                    ))
        )
    }

    fun getPriceToday(url: String): PricePerDateDto{
        val doc: Document = Jsoup.connect(url).get()
        val productPrice = getPrice(doc)

        return PricePerDateDto(
            productPrice,
            Date(Calendar.getInstance().time.time)
        )

    }

    companion object{

        val providers = listOf("emag", "evomag", "elefant", "altex", "fashiondays", "price")

        fun getProviderName(url: String): String{
            val prov = providers.filter { url.contains(it) }
            if(prov.isEmpty()){
                throw NoExistingProviderException("No existing provider for this link")
            }
            return prov[0]
        }
    }

}