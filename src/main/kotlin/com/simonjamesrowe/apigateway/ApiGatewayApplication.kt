package com.simonjamesrowe.apigateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ApiGatewayApplication

fun main(args: Array<String>) {
  runApplication<ApiGatewayApplication>(*args)
}
