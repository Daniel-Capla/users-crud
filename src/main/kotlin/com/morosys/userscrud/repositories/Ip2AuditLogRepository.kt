package com.morosys.userscrud.repositories

import com.morosys.userscrud.models.IpAuditLog
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface Ip2AuditLogRepository : CrudRepository<IpAuditLog, UUID> {
}