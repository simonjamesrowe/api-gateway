package com.simonjamesrowe.apigateway.test.restcontroller

import com.ninjasquad.springmockk.MockkBean
import com.simonjamesrowe.apigateway.config.SecurityConfig
import com.simonjamesrowe.apigateway.core.usecase.ResumeUseCase
import com.simonjamesrowe.apigateway.entrypoints.restcontroller.ResumeController
import io.mockk.coEvery
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.core.io.ClassPathResource
import org.springframework.test.web.reactive.server.WebTestClient
import java.nio.file.Files

@WebFluxTest(ResumeController::class)
@Import(SecurityConfig::class)
internal class ResumeControllerTest {

  @MockkBean
  lateinit var resumeUseCase: ResumeUseCase

  @Autowired
  private lateinit var webClient: WebTestClient

  @Test
  fun `should return file`() {
    val bytes = Files.readAllBytes(ClassPathResource("resume.pdf").file.toPath())
    coEvery { resumeUseCase.getResume() } returns bytes

    val actual = webClient.get().uri("/resume")
      .exchange()
      .expectStatus().isOk
      .expectBody().returnResult().responseBody

    assertThat(actual).isEqualTo(bytes)
  }

}
