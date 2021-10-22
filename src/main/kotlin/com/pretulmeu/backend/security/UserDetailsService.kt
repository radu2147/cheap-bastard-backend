package com.pretulmeu.backend.security

import com.pretulmeu.backend.impl.repo.UserRepo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
internal class UserDetailsService(val userRepo: UserRepo): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? {
        return username?.let{
            val user = userRepo.findByUsername(it)
            return user?.let { com.pretulmeu.backend.security.UserDetails(user.username, user.password) }
        }
    }
}