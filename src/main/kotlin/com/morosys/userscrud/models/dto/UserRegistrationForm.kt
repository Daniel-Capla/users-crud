package com.morosys.userscrud.models.dto

data class UserRegistrationForm(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val userName: String? = null
)
