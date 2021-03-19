package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import com.ninjasquad.springmockk.MockkBean
import com.simonjamesrowe.apigateway.config.SecurityConfig
import com.simonjamesrowe.apigateway.core.model.ContactUsRequest
import com.simonjamesrowe.apigateway.core.usecase.ContactUseCase
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(ContactUsController::class)
@Import(SecurityConfig::class)
internal class ContactUsControllerTest {

  @MockkBean(relaxed = true)
  lateinit var contactUseCase: ContactUseCase

  @Autowired
  private lateinit var webClient: WebTestClient

  @Test
  fun `should sucessfully handle contact us request`() = runBlocking<Unit> {
    webClient
      .post()
      .uri("/contact-us")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Referer", "Referer")
      .bodyValue(
        """
         {
          "firstName" : "Simon",
          "lastName": "Rowe",
          "subject" : "Hi",
          "emailAddress" : "simon.rowe@gmail.com",
          "content" : "This is a test message"
        }
       """.trimIndent()
      )
      .exchange()
      .expectStatus().is2xxSuccessful

    coVerify {
      contactUseCase.contactUs(
        ContactUsRequest(
          first = "Simon",
          last = "Rowe",
          email = "simon.rowe@gmail.com",
          subject = "Hi",
          message = "This is a test message",
          referrer = "Referer"
        )
      )
    }
  }

  @ParameterizedTest
  @CsvSource(
    "'',Rowe,Hello,simon.rowe@gmail.com,Message",
    "Simon,'',Hello,simon.rowe@gmail.com,Message",
    "Simon,Rowe,'',simon.rowe@gmail.com,Message",
    "Simon,Rowe,Hello,'',Message",
    "Simon,Rowe,Hello,simon.rowe@gmail.com,''",
    "Simon,Rowe,Hello,invalidEmailAddress,Message"
  )
  fun `client error should occur for invalid fields`(
    firstName: String,
    lastName: String,
    subject: String,
    email: String,
    content: String
  ) {
    val requestBody = """
      {
        "firstName" : "$firstName",
        "lastName": "$lastName",
        "subject" : "$subject",
        "emailAddress" : "$email",
        "content" : "$content"
      }
    """.trimIndent()
    webClient
      .post()
      .uri("/contact-us")
      .contentType(MediaType.APPLICATION_JSON)
      .header("Referer", "Referer")
      .bodyValue(requestBody)
      .exchange()
      .expectStatus().is4xxClientError
  }
}
