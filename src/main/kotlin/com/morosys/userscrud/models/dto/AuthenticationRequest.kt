package com.morosys.userscrud.models.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AuthenticationRequest(
    @NotBlank val username: String,
    @NotBlank @Size(min = 6, max = 20) val password: String
)
