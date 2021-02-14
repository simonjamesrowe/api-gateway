package com.simonjamesrowe.apigateway.controller

import com.ninjasquad.springmockk.MockkBean
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.simonjamesrowe.component.test.BaseComponentTest
import com.simonjamesrowe.component.test.kafka.WithKafkaContainer
import io.mockk.verify
import io.restassured.RestAssured.given
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.annotation.DirtiesContext

@WithKafkaContainer
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
internal class ContactUsControllerTest : BaseComponentTest() {

  @MockkBean(relaxed = true)
  private lateinit var sendGrid: SendGrid

  @Test
  fun `email should be sent with valid input`() {
    val requestBody = """
      {
        "firstName" : "Simon",
        "lastName": "Rowe",
        "subject" : "Hi",
        "emailAddress" : "simon.rowe@gmail.com",
        "content" : "This is a test message"
      }
    """.trimIndent()
    given()
      .log().all()
      .contentType("application/json")
      .body(requestBody)
      .post("/contact-us")
      .then()
      .log().all()
      .statusCode(200)


    val requests = mutableListOf<Request>()
    verify { sendGrid.api(capture(requests)) }
    assertThat(requests).hasSize(1)
    assertThat(requests[0].endpoint).isEqualTo("mail/send")
    val expectedBody = """
      {"from":{"email":"simon@simonjamesrowe.com"},"subject":"Hi","personalizations":[{"to":[{"email":"simon.rowe@gmail.com"}]}],"content":[{"type":"text/plain","value":"A message has been sent from the site: null\nEmail Address: simon.rowe@gmail.com\nName: Simon Rowe\nContent: This is a test message"}]}
    """.trimIndent()
    JSONAssert.assertEquals(expectedBody, requests[0].body, false)
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
    given()
      .log().all()
      .contentType("application/json")
      .body(requestBody)
      .post("/contact-us")
      .then()
      .log().all()
      .statusCode(400)
  }

}
