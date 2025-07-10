package com.morosys.userscrud.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    @Column(unique = true)
    val userName: String? = null,
    @Column(unique = true)
    val email: String,
    @JsonIgnore
    var password: String,
    @Transient
    val accessToken: String,
    @CreationTimestamp
    val createdAt: Instant? = null,
    @UpdateTimestamp
    val lastUpdatedAt: Instant? = null,
    val deletedAt: Instant? = null
)
