package com.simonjamesrowe.apigateway.core.model

data class ContactUsRequest(
  val first: String,
  val last: String,
  val email: String,
  val subject: String,
  val message: String,
  val referrer: String?
)

