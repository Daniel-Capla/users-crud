package com.morosys.userscrud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UsersCrudApplication

fun main(args: Array<String>) {
	runApplication<UsersCrudApplication>(*args)
}
