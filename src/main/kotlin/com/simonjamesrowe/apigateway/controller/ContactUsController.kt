package com.simonjamesrowe.apigateway.controller

import com.sendgrid.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
class ContactUsController(
  private val sendGrid: SendGrid,
  @Value("\${sendgrid.email.from:}")
  private val fromAddress: String,
  @Value("\${sendgrid.email.to:}")
  private val toAddress: String
) {

  companion object {
    val log = LoggerFactory.getLogger(ContactUsController::class.java)
  }

  @PostMapping("/contact-us")
  fun webhookPost(
    @RequestBody @Valid contactUs: ContactUs,
    @RequestHeader(value = "Referer", required = false) referer: String?
  ) {
    log.info("Contact Us request made: $contactUs")
    val mail = Mail(
      com.sendgrid.Email(fromAddress),
      contactUs.subject,
      com.sendgrid.Email(toAddress),
      Content(
        "text/plain",
        """
          A message has been sent from the site: $referer
          Email Address: ${contactUs.emailAddress}
          Name: ${contactUs.firstName} ${contactUs.lastName}
          Content: ${contactUs.content}
        """.trimIndent()
      )
    )
    runCatching {
      sendMail(mail)
    }.getOrElse {
      log.error("Error sending email: $contactUs", it)
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

  data class ContactUs(
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    @field:Email
    val emailAddress: String,
    @field:NotBlank
    val subject: String,
    @field:NotBlank
    val content: String
  )
}
