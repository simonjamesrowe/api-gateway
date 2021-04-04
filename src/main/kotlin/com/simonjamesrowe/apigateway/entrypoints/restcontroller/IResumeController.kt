package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import org.springframework.http.ResponseEntity

interface IResumeController {
  suspend fun resume(): ResponseEntity<ByteArray>
}
