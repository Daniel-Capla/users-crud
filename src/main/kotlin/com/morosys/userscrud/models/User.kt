package com.morosys.userscrud.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    val userName: String? = null,
    val email: String,
    val password: String,
    @Transient
    val accessToken: String
)
