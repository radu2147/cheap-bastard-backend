package com.pretulmeu.backend.impl.model

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
open class Entity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long
)