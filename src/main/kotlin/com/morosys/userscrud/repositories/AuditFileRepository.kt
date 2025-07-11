package com.morosys.userscrud.repositories

import com.morosys.userscrud.models.AuditFile
import org.springframework.data.repository.CrudRepository

interface AuditFileRepository : CrudRepository<AuditFile, Int> {
}