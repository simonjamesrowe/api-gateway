package com.simonjamesrowe.apigateway.dataproviders.sendgrid

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.simonjamesrowe.apigateway.core.model.Email
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert
import java.lang.RuntimeException

@ExtendWith(MockKExtension::class)
internal class SendGridEmailSenderTest {

  @RelaxedMockK
  private lateinit var sendGrid: SendGrid

  private lateinit var sendGridEmailSender: SendGridEmailSender

  @BeforeEach
  fun before() {
    sendGridEmailSender = SendGridEmailSender(sendGrid, "from@from.com", "to@to.com")
  }

  @Test
  fun `should send email`() = runBlocking<Unit> {
    val email = Email(
      subject = "subject",
      contentType = "text/plain",
      body = "Email body"
    )

    val request = mutableListOf<Request>()

    sendGridEmailSender.sendEmail(email)

    verify { sendGrid.api(capture(request)) }
    assertThat(request[0].endpoint).isEqualTo("mail/send")
    assertThat(request[0].method).isEqualTo(Method.POST)
    val expectedBody =
      """{"from":{"email":"from@from.com"},"subject":"subject","personalizations":[{"to":[{"email":"to@to.com"}]}],"content":[{"type":"text/plain","value":"Email body"}]}"""
    JSONAssert.assertEquals(request[0].body, expectedBody, true)
  }

  @Test
  fun `should throw exception if email fails to send`() = runBlocking<Unit> {
    val email = Email(
      subject = "subject",
      contentType = "text/plain",
      body = "Email body"
    )

    every { sendGrid.api(any())} throws RuntimeException("exception")

    assertThrows<RuntimeException> {sendGridEmailSender.sendEmail(email)}
  }

}
