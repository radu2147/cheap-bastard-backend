package com.pretulmeu.backend.impl.providers

import com.pretulmeu.backend.impl.exceptions.InvalidSelectorException
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class AltexShopProvider: ShopProvider() {

    override val name = "altex"

    override fun getImage(doc: Document): String {
        val el = doc.select(".product-view .gallery .slick-list img").map { it.attr("src") }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getProductNameTag(doc: Document): String {
        val el = doc.select(".product-view h1").map { if(it.text().length < 100) it.text() else it.text().subSequence(0,100).toString() + "..." }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getPrice(doc: Document): Float {
        val el = doc.select(".Price-current").map { it.text() }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0].split(",")[0].toFloat()
    }
}