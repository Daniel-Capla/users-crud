package com.morosys.userscrud.configuration

import com.morosys.userscrud.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler
    fun handleExceptions(exception: Throwable): ResponseEntity<String> {
        return when (exception) {
            is NotFoundException -> ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.message ?: "Not found")
            //TODO need to be more robust in order to be done
            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.message)
        }
    }
}
