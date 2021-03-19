package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import com.simonjamesrowe.apigateway.core.model.ContactUsRequest
import com.simonjamesrowe.apigateway.core.usecase.ContactUseCase
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@RestController
class ContactUsController(
  private val contactUseCase: ContactUseCase
) {

  val log = LoggerFactory.getLogger(ContactUsController::class.java)

  @PostMapping("/contact-us")
  suspend fun webhookPost(
    @RequestBody @Valid contactUs: ContactUs,
    @RequestHeader(value = "Referer", required = false) referer: String?
  ) {
    log.info("Contact Us request made: $contactUs")
    contactUseCase.contactUs(
      ContactUsRequest(
        first = contactUs.firstName,
        last = contactUs.lastName,
        email = contactUs.emailAddress,
        subject = contactUs.subject,
        message = contactUs.content,
        referrer = referer
      )
    )
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
