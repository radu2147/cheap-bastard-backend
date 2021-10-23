package com.pretulmeu.backend.impl.providers

import com.pretulmeu.backend.impl.exceptions.InvalidSelectorException
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component


@Component
class EmagShopProvider: ShopProvider() {
    override val name: String = "emag"

    override fun getImage(doc: Document): String {
        val el =  doc.select("#product-gallery img").map { it.attr("src") }
        if(el.size != 1){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getProductNameTag(doc: Document): String {
        val el = doc.select(".page-title").map { if(it.text().length < 100) it.text() else it.text().subSequence(0,100).toString() + "..." }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getPrice(doc: Document): Float {
        val el = doc.select("form.main-product-form .product-new-price").map { it.textNodes()[0].text() }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0].replace(".", "").toFloat()
    }
}