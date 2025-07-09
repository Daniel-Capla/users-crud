package com.morosys.userscrud.controllers

import com.morosys.userscrud.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) {
    @GetMapping("/users")
    fun findAllUsers(

    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.findAllUsers().toString())
    }

    @PostMapping("/register-user")
    fun registerUser(
        @RequestBody userForm: String
    ): ResponseEntity<String> {

    }
}
