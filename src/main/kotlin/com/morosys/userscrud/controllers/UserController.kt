package com.morosys.userscrud.controllers

import com.morosys.userscrud.exceptions.NotFoundException
import com.morosys.userscrud.models.User
import com.morosys.userscrud.models.dto.AuthenticationRequest
import com.morosys.userscrud.models.dto.UserRegistrationForm
import com.morosys.userscrud.models.enums.UserRole
import com.morosys.userscrud.services.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
) {
    @GetMapping("/users")
    fun findAllUsers(

    ): ResponseEntity<List<User>> {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.findAll())
    }

    /**
     * Ability to find user and get user info is available only for COMMUNITY access and higher
     */
    @GetMapping("/user")
    fun findUser(
        @AuthenticationPrincipal user: User?,
        @RequestParam @org.hibernate.validator.constraints.UUID id: String
    ): ResponseEntity<User> {
        if (user?.id.toString() != id && user?.role == UserRole.USER) {
            throw AccessDeniedException("You are not allowed to view this page")
        }
        val userFromParameter = userService.findById(UUID.fromString(id))

        return when (userFromParameter) {
            null -> throw NotFoundException("User not found")
            else -> ResponseEntity.status(HttpStatus.FOUND).body(userFromParameter)
        }
    }

    @PostMapping("/register")
    fun register(
        request: HttpServletRequest,
        @RequestBody @Valid userForm: UserRegistrationForm
    ): ResponseEntity<User> {
        val registeredUser = userService.register(userForm, request)

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser)
    }

    /**
     * USER should be able to update only their own profile
     * ADMIN can update anyone's profile
     */
    @PutMapping("/update")
    fun update(
        @AuthenticationPrincipal user: User,
        @RequestParam @org.hibernate.validator.constraints.UUID id: String? = null,
        @RequestParam password: String? = null,
        @RequestParam @Valid @Size(min = 3, max = 20) username: String? = null
    ): ResponseEntity<User> {
        if (id != null && user.id.toString() != id && user.role != UserRole.ADMIN) {
            throw AccessDeniedException("You are not allowed to view this page")
        }

        val userToUpdate = id?.let { userService.findById(UUID.fromString(id)) } ?: user
        val updatedUser = userService.update(userToUpdate, password, username)

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser)
    }

    @DeleteMapping("/delete")
    fun delete(
        @RequestParam @org.hibernate.validator.constraints.UUID id: String
    ): ResponseEntity<String> {
        userService.softDelete(UUID.fromString(id))

        return ResponseEntity.status(HttpStatus.OK).body("Deleted!")
    }

    @PostMapping("/login")
    fun login(
        httpServletRequest: HttpServletRequest,
        @RequestBody @Valid authenticationRequest: AuthenticationRequest
    ): ResponseEntity<User> {
        val user = userService.login(authenticationRequest, httpServletRequest)

        return ResponseEntity.status(HttpStatus.OK).body(user)
    }

    @DeleteMapping("/admin-delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun adminDelete(
        @RequestParam @org.hibernate.validator.constraints.UUID id: String
    ): ResponseEntity<String> {
        userService.hardDelete(UUID.fromString(id))

        return ResponseEntity.status(HttpStatus.OK).body("ADMIN deleted!")
    }

    //TODO add validation of input fields,
// proper login + registration - DONE,
// add env variables - DONE,
// add JWT security - DONE,
// add User soft delete + real delete (maybe new table for audit purposes) - DONE,
// add tests,
// add github actions ci/cd?,
// add ip2location? - DONE
}
