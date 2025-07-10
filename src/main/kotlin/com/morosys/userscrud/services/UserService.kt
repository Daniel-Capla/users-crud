package com.morosys.userscrud.services

import com.morosys.userscrud.exceptions.NotFoundException
import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.repositories.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findAll(): List<User> {
        return userRepository.findAll().toList()
    }

    fun findById(id: UUID): User? {
        return userRepository.findById(id).getOrNull()
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

    fun update(id: UUID, newPassword: String): User {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        userInDb.password = newPassword

        return userRepository.save(userInDb)
    }

    fun delete(id: UUID) {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        userRepository.delete(userInDb)
    }

    private fun generateGenericUserName(firstName: String, lastName: String, email: String): String {
        //TODO to be implemented
        return ""
    }
}