package com.morosys.userscrud.services

import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findAll(): List<User> {
        return userRepository.findAll().toList()
    }

    fun register(userRegistrationForm: UserRegistrationForm): User {
        val savedUser = userRepository.save(
            User(
                firstName = userRegistrationForm.firstName,
                lastName = userRegistrationForm.lastName,
                email = userRegistrationForm.email,
                password = userRegistrationForm.password,
                userName = userRegistrationForm.userName ?: generateGenericUserName(
                    userRegistrationForm.firstName,
                    userRegistrationForm.lastName,
                    userRegistrationForm.email
                ),
                accessToken = "" //TODO to be implemented
            )
        )

        return savedUser
    }

    private fun generateGenericUserName(firstName: String, lastName: String, email: String): String {
        //TODO to be implemented
        return ""
    }
}