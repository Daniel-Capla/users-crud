package com.morosys.userscrud.models.dto

data class AuthenticationResponse(
    val accessToken: String,
    val refreshToken: String
)