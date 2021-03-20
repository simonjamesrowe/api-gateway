package com.simonjamesrowe.apigateway.core.usecase

import com.simonjamesrowe.apigateway.core.model.ContactUsRequest
import com.simonjamesrowe.apigateway.core.model.Email
import com.simonjamesrowe.apigateway.core.repository.EmailSender
import org.springframework.stereotype.Service

@Service
class ContactUseCase(
  private val emailSender: EmailSender
) {

  suspend fun contactUs(contactUsRequest: ContactUsRequest) {
    val email = Email(
      subject = contactUsRequest.subject,
      contentType = "text/plain",
      body = textPlainBody(contactUsRequest)
    )
    emailSender.sendEmail(email)
  }

  private fun textPlainBody(contactUsRequest: ContactUsRequest) =
    """
      A message has been sent from the site: ${contactUsRequest.referrer}
      Email Address: ${contactUsRequest.email}
      Name: ${contactUsRequest.first} ${contactUsRequest.last}
      Content: ${contactUsRequest.message}
    """.trimIndent()

}
