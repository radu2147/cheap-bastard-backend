package com.pretulmeu.backend.impl.providers

import com.pretulmeu.backend.impl.exceptions.InvalidSelectorException
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class ElefantShopProvider: ShopProvider() {

    override val name = "elefant"

    override fun getImage(doc: Document): String {
        val el =  doc.select("#prodimgcarousel img").map { it.attr("src") }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getProductNameTag(doc: Document): String {
        val el = doc.select(".product-details .product-title").map { if(it.text().length < 100) it.text() else it.text().subSequence(0,100).toString() + "..." }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0]
    }

    override fun getPrice(doc: Document): Float {
        val el = doc.select(".pdp-table .current-price").map { it.textNodes()[0].text().split(" ")[0] }
        if(el.isEmpty()){
            throw InvalidSelectorException("Invalid selector for provider with name ${name}")
        }
        return el[0].replace(',', '.').toFloat()
    }
}