package com.pretulmeu.backend.impl.repo

import com.pretulmeu.backend.impl.model.PricePerDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface PricePerDateRepo: JpaRepository<PricePerDate, Long> {
}