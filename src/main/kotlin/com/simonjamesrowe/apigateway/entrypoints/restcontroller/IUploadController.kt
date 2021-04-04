package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import org.springframework.cloud.gateway.webflux.ProxyExchange
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity

interface IUploadController {

  suspend fun proxy(
    file: String,
    headers: HttpHeaders,
    proxy: ProxyExchange<ByteArray?>
  ): ResponseEntity<ByteArray?>?
}
