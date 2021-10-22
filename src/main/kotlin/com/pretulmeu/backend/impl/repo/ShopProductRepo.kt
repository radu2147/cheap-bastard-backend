package com.pretulmeu.backend.impl.repo

import com.pretulmeu.backend.impl.model.ShopProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query


internal interface ShopProductRepo: JpaRepository<ShopProduct, Long> {
    @Modifying
    @Query("delete from ShopProduct sp where sp.id = :entityId")
    override fun deleteById(entityId: Long)
}