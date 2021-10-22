package com.pretulmeu.backend.impl.model

import javax.persistence.Column
import javax.persistence.OneToMany
import javax.persistence.Table

@javax.persistence.Entity
@Table(name = "app_user")
internal class User(
    id: Long = -1,

    @Column(name = "username")
    val username: String,

    @Column(name = "password")
    val password: String
): Entity(id) {

    @OneToMany(mappedBy = "user")
    var produss: Set<Produs>? = null
}