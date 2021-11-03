package com.pretulmeu.backend.impl.model

import com.pretulmeu.backend.api.dto.ProdusDto
import javax.persistence.*

@javax.persistence.Entity
@Table(name = "produs")
internal class Produs(
    id: Long,

    @Column(name = "name")
    val name: String,

    @Column(name = "img_name", length = 500)
    val img_name: String?,

    @JoinColumn(name = "user_id")
    @ManyToOne
    val user: User
): Entity(id){

    @OneToMany(mappedBy = "product", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE], orphanRemoval = true)
    var shopProducts: Set<ShopProduct>? = null

    fun toDto(): ProdusDto{
        return ProdusDto(this.id,
            this.name,
            this.img_name,
            this.shopProducts?.map { it.toDto() }?.toSet(),
            this.computeLowestPrice()
            )
    }

    private fun computeLowestPrice(): Float? {
        return this.shopProducts?.map { it.history?.maxByOrNull { it.date }?.price ?: Float.MAX_VALUE}?.minByOrNull { it }
    }
}