package com.morosys.userscrud.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

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

    @PostMapping("/register-user")
    fun registerUser(
        @RequestBody userForm: String
    ): ResponseEntity<User> {
        val userRegistrationForm = objectMapper.readValue(userForm, UserRegistrationForm::class.java)
        val registeredUser = userService.register(userRegistrationForm)

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
    }
}
