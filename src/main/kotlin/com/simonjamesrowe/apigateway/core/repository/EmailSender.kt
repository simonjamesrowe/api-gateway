package com.simonjamesrowe.apigateway.core.repository

import com.simonjamesrowe.apigateway.core.model.Email

interface EmailSender {
  suspend fun sendEmail(email: Email)
}
