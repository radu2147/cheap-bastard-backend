package com.pretulmeu.backend.impl.repo

import com.pretulmeu.backend.api.dto.ShopProductDto
import com.pretulmeu.backend.impl.model.Produs
import com.pretulmeu.backend.impl.model.ShopProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
internal interface ProdusRepo: JpaRepository<Produs, Long>{

    @Query("""
        select pr from Produs pr where (:name IS NOT NULL AND pr.user.username=:name)
    """)
    fun findAllByUsername(name: String?): List<Produs>


}