package com.morosys.userscrud.models.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UserRegistrationForm(
    @field:Email
    val email: String,
    val firstName: String,
    val lastName: String,
    @field:Size(min = 6, max = 12)
    val password: String,
    val userName: String? = null
)
