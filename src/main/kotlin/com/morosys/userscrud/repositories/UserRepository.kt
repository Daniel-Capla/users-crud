package com.morosys.userscrud.repositories

import com.morosys.userscrud.models.User
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserRepository : CrudRepository<User, UUID> {
}