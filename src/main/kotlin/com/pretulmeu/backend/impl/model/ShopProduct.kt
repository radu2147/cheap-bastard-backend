package com.pretulmeu.backend.impl.model

import com.pretulmeu.backend.api.dto.ShopProductDto
import javax.persistence.*

@javax.persistence.Entity
@Table(name = "shop_product")
internal class ShopProduct(
    id: Long,

    @Column(name = "name")
    val name: String,

    @Column(name = "link", length = 500)
    val link: String?,

    @JoinColumn(name = "prod_id")
    @ManyToOne
    val product: Produs
): Entity(id){

    @OneToMany(mappedBy = "produs", cascade = [CascadeType.MERGE])
    var history: MutableSet<PricePerDate>? = null

    fun toDto(): ShopProductDto{
        return ShopProductDto(this.id, this.name, this.link, this.history?.map { it.toDto() }?.toSet())
    }

}