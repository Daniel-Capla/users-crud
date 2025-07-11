package com.morosys.userscrud.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.morosys.userscrud.exceptions.NotFoundException
import com.morosys.userscrud.models.AuditFile
import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.AuthenticationRequest
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.repositories.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(
    private val auditFileService: AuditFileService,
    private val tokenService: TokenService,
    private val authenticationService: AuthenticationService,
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    @Value("\${jwt.accessTokenExpiration}")
    private val accessTokenExpiration: Long = 0,
    private val passwordEncoder: PasswordEncoder,
) {
    fun findAll(): List<User> {
        return userRepository.findAll().filter { user -> user.deletedAt == null }.toList()
    }

    fun findById(id: UUID): User? {
        return userRepository.findById(id).getOrNull()
    }

    fun findByUserName(userName: String): User? {
        return userRepository.findByUserName(userName)
    }

    fun register(userRegistrationForm: UserRegistrationForm): User {
        val username = processRegistrationUsername(userRegistrationForm.userName, userRegistrationForm.email)
        val savedUser = userRepository.save(
            User(
                firstName = userRegistrationForm.firstName,
                lastName = userRegistrationForm.lastName,
                email = userRegistrationForm.email,
                password = passwordEncoder.encode(userRegistrationForm.password),
                userName = username,
                accessToken = tokenService.generateToken(
                    subject = username,
                    expiration = Date(System.currentTimeMillis() + accessTokenExpiration)
                )
            )
        )

        return savedUser
    }

    fun login(authenticationRequest: AuthenticationRequest): User {
        val user = findByUserName(authenticationRequest.username) ?: throw NotFoundException("User not found!")
        val authenticationResponse = authenticationService.authentication(authenticationRequest)

        user.accessToken = authenticationResponse.accessToken

        return user
    }

    fun update(id: UUID, newPassword: String?, newUserName: String?): User {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        newPassword?.let { userInDb.password = it }
        newUserName?.let { userInDb.userName = processNewUserNameRequest(newUserName) }

        return userRepository.save(userInDb)
    }

    fun delete(id: UUID) {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        // Soft delete first
        userInDb.deletedAt = Instant.now()

        userRepository.save(userInDb)
    }

    fun adminDelete(id: UUID) {
        val userInDb = userRepository.findById(id).getOrNull() ?: throw NotFoundException("User not found")
        auditFileService.createNew(
            AuditFile(
                json = objectMapper.writeValueAsString(userInDb)
            )
        )

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

    private fun processRegistrationUsername(username: String?, email: String): String {
        return username?.let {
            when (checkIfUserNameTaken(it)) { // If username exists, generate random userName
                true -> generateRandomUserName(email)
                else -> it
            }
        } ?: generateRandomUserName(email)
    }
}
