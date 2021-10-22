package com.pretulmeu.backend.impl.model

import com.pretulmeu.backend.api.dto.PricePerDateDto
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.sql.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity(name = "price_product")
internal class PricePerDate(

    id: Long,

    @Column(name = "price")
    val price: Float,

    @Column(name = "date")
    val date: Date,

    @JoinColumn(name = "shop_prod_id")
    @ManyToOne
    val produs: ShopProduct
): com.pretulmeu.backend.impl.model.Entity(id){
    fun toDto(): PricePerDateDto{
        return PricePerDateDto(this.price, this.date)
    }
}