package com.simonjamesrowe.apigateway.core.model

data class Email(
  val subject: String,
  val contentType: String,
  val body: String
)
