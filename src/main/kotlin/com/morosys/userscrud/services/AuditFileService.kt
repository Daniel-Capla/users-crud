package com.morosys.userscrud.services

import com.morosys.userscrud.models.AuditFile
import com.morosys.userscrud.repositories.AuditFileRepository
import org.springframework.stereotype.Service

@Service
class AuditFileService(
    private val auditFileRepository: AuditFileRepository
) {
    fun createNew(auditFile: AuditFile) {
        auditFileRepository.save(auditFile)
    }
}
