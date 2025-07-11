package com.morosys.userscrud.services

import com.morosys.userscrud.repositories.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException

class JwtUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUserName(username) ?: throw UsernameNotFoundException("$username not found!")

        return User.builder()
            .username(user.userName)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }
}
