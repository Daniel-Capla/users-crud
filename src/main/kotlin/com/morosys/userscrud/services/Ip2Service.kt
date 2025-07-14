package com.morosys.userscrud.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.morosys.userscrud.exceptions.IpServiceException
import com.morosys.userscrud.models.IpAuditLog
import com.morosys.userscrud.models.User
import com.morosys.userscrud.repositories.Ip2AuditLogRepository
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class Ip2Service(
    private val ip2AuditLogRepository: Ip2AuditLogRepository,
    private val objectMapper: ObjectMapper,
    @Value("\${ip2location.apiKey}")
    private val ip2ApiKey: String
) {
    val ip2Url = "https://api.ip2location.io/?key="

    fun getIpLocationDetails(user: User, ip: String?) {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().get().url("""$ip2Url$ip2ApiKey&ip=$ip""".toHttpUrl()).build()

        try {
            val response = client.newCall(request).execute()
            val responseString = response.body?.string()
            if (response.isSuccessful) {
                val country = objectMapper.readTree(responseString).path("country_name")
                val countryCode = objectMapper.readTree(responseString).path("country_code")
                val auditLog = IpAuditLog(
                    user = user,
                    ipAddress = ip,
                    country = country.asText(),
                    countryCode = countryCode.asText(),
                    rawData = responseString!!
                )
                ip2AuditLogRepository.save(auditLog)
            } else throw IpServiceException(responseString ?: "Ip2Location service returned error")
        } catch (e: Exception) {
            // if the exception is of type IpServiceException, just consume it. Otherwise throw the error, needs to be handled
            when {
                e is IpServiceException -> println(e)
                else -> throw e
            }
        }
    }
}
