package com.simonjamesrowe.apigateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source

@SpringBootApplication
@EnableBinding(Source::class)
class ApiGatewayApplication

fun main(args: Array<String>) {
	runApplication<ApiGatewayApplication>(*args)
}
