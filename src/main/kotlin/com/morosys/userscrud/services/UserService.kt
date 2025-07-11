package com.morosys.userscrud.services

import com.morosys.userscrud.exceptions.NotFoundException
import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.repositories.UserRepository
import org.apache.coyote.BadRequestException
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

    fun findByUserName(userName: String): User? {
        return userRepository.findByUserName(userName)
    }

    fun register(userRegistrationForm: UserRegistrationForm): User {
        val savedUser = userRepository.save(
            User(
                firstName = userRegistrationForm.firstName,
                lastName = userRegistrationForm.lastName,
                email = userRegistrationForm.email,
                password = userRegistrationForm.password,
                userName = userRegistrationForm.userName?.let { // If username exists, generate random userName
                    when (checkIfUserNameTaken(it)) {
                        true -> generateRandomUserName(userRegistrationForm.email)
                        else -> it
                    }
                } ?: generateRandomUserName(userRegistrationForm.email),
                accessToken = "" //TODO to be implemented
            )
        )

        return savedUser
    }

    fun update(id: UUID, newPassword: String?, newUserName: String?): User {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        newPassword?.let { userInDb.password = it }
        newUserName?.let { userInDb.userName = processNewUserNameRequest(newUserName) }

        return userRepository.save(userInDb)
    }

    fun delete(id: UUID) {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        userRepository.delete(userInDb)
    }

    private fun generateRandomUserName(email: String): String {
        val emailString = email.substring(0, 2)
        var generatedUserName = emailString + generateRandomString(3)

        while (checkIfUserNameTaken(generatedUserName) == true) {
            generatedUserName = emailString + generateRandomString(3)
        }

        return generatedUserName
    }

    private fun generateRandomString(length: Int): String {
        val chars = ('A'..'Z') + ('0'..'9')
        var result = ""
        while (result.length < length) {
            result += chars.random()
        }

        return result
    }

    private fun checkIfUserNameTaken(userName: String): Boolean {
        val user = findByUserName(userName)

        return when (user) {
            null -> false
            else -> true
        }
    }

    private fun processNewUserNameRequest(userName: String): String {
        return when (checkIfUserNameTaken(userName)) {
            true -> throw BadRequestException("Username already taken!")
            else -> userName
        }
    }
}