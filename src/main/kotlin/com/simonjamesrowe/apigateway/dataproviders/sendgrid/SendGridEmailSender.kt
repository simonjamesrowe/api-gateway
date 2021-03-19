package com.simonjamesrowe.apigateway.dataproviders.sendgrid

import com.sendgrid.*
import com.simonjamesrowe.apigateway.core.model.Email
import com.simonjamesrowe.apigateway.core.repository.EmailSender
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SendGridEmailSender(
  private val sendGrid: SendGrid,
  @Value("\${sendgrid.email.from:}")
  private val fromAddress: String,
  @Value("\${sendgrid.email.to:}")
  private val toAddress: String
) : EmailSender {

  val log = LoggerFactory.getLogger(SendGridEmailSender::class.java)

  override suspend fun sendEmail(email: Email) = coroutineScope {
    val mail = Mail(
      Email(fromAddress),
      email.subject,
      Email(toAddress),
      Content(email.contentType, email.body)
    )
    runCatching {
      withContext(IO) { sendMail(mail) }
    }.getOrElse {
      log.error("Error sending email: $email", it)
      throw it
    }
  }

  private fun sendMail(mail: Mail) {
    val request = Request().also {
      it.method = Method.POST
      it.endpoint = "mail/send"
      it.body = mail.build()
    }
    sendGrid.api(request)
  }

}
