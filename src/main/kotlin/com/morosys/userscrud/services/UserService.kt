package com.morosys.userscrud.services

import com.morosys.userscrud.models.User
import com.morosys.userscrud.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }
}