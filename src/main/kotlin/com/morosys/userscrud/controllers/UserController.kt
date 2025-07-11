package com.morosys.userscrud.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.morosys.userscrud.exceptions.NotFoundException
import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.services.UserService
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class UserController(
    private val userService: UserService,
    private val objectMapper: ObjectMapper
) {
    @GetMapping("/users")
    fun findAllUsers(

    ): ResponseEntity<List<User>> {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.findAll())
    }

    @GetMapping("/user")
    fun findUser(
        @RequestParam id: String
    ): ResponseEntity<User> {
        val user = userService.findById(UUID.fromString(id))

        return when (user) {
            null -> throw NotFoundException("User not found")
            else -> ResponseEntity.status(HttpStatus.FOUND).body(user)
        }
    }

    @PostMapping("/register")
    fun register(
        @RequestBody @Valid userForm: UserRegistrationForm
    ): ResponseEntity<User> {
        val registeredUser = userService.register(userForm)

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
    }

    @PutMapping("/update")
    fun update(
        @RequestParam id: String,
        @RequestParam password: String? = null,
        @RequestParam @Valid @Size(min = 3, max = 20) userName: String? = null
    ): ResponseEntity<User> {
        val updatedUser = userService.update(UUID.fromString(id), password, userName)

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser)
    }

    @DeleteMapping("/delete")
    fun delete(
        @RequestParam id: String
    ): ResponseEntity<String> {
        userService.delete(UUID.fromString(id))

        return ResponseEntity.status(HttpStatus.OK).body("Deleted!")
    }

    //TODO add validation of input fields, proper login + registration, add env variables, add JWT security, add User soft delete + real delete (maybe new table for audit purposes), add tests, add github actions ci/cd?, add ip2location?
}
