package com.pretulmeu.backend.impl.providers

import com.pretulmeu.backend.impl.exceptions.InvalidSelectorException
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class EvomagShopProvider: ShopProvider() {

    override val name = "evomag"

    override fun getImage(doc: Document): String {
        val el =  doc.select(".product_info_area .MainProductGallery img").map { it.attr("src") }
        if(el.size != 1){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getProductNameTag(doc: Document): String {
        val el = doc.select(".product_info_area .product_name").map { if(it.text().length < 100) it.text() else it.text().subSequence(0,100).toString() + "..." }
        if(el.size != 1){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getPrice(doc: Document): Float {
        val el = doc.select(".product_info_area .pret_rons").map { it.textNodes()[0].text() }
        if(el.size != 1){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0].split(",")[0].toFloat()
    }
}