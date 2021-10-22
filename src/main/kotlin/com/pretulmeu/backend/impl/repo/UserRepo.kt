package com.pretulmeu.backend.impl.repo

import com.pretulmeu.backend.impl.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
internal interface UserRepo: JpaRepository<User, Long>{
    fun findByUsername(username: String): User?
}