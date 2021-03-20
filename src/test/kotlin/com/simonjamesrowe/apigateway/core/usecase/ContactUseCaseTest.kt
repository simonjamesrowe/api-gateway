package com.simonjamesrowe.apigateway.core.usecase

import com.simonjamesrowe.apigateway.TestUtils
import com.simonjamesrowe.apigateway.TestUtils.randomObject
import com.simonjamesrowe.apigateway.core.model.ContactUsRequest
import com.simonjamesrowe.apigateway.core.model.Email
import com.simonjamesrowe.apigateway.core.repository.EmailSender
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ContactUseCaseTest {

  @RelaxedMockK
  lateinit var emailSender: EmailSender

  @InjectMockKs
  lateinit var contactUseCase: ContactUseCase

  @Test
  fun `should generate email body and send email`() = runBlocking<Unit> {
    val request = randomObject<ContactUsRequest>()
    contactUseCase.contactUs(request)

    val email = mutableListOf<Email>()
    coVerify { emailSender.sendEmail(capture(email)) }

    assertThat(email[0].contentType).isEqualTo("text/plain")
    assertThat(email[0].subject).isEqualTo(request.subject)
    assertThat(email[0].body).isEqualTo(
      """
      A message has been sent from the site: ${request.referrer}
      Email Address: ${request.email}
      Name: ${request.first} ${request.last}
      Content: ${request.message}
    """.trimIndent()
    )
  }

}
