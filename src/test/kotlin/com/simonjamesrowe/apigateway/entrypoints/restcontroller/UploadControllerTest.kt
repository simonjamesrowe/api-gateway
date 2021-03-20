package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import com.github.tomakehurst.wiremock.WireMockServer
import com.ninjasquad.springmockk.MockkBean
import com.simonjamesrowe.apigateway.TestUtils.mockGetJpg
import com.simonjamesrowe.apigateway.config.SecurityConfig
import com.simonjamesrowe.apigateway.core.usecase.CompressFileUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Import
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(UploadController::class)
@ActiveProfiles("cms")
@Import(SecurityConfig::class)
@AutoConfigureWireMock(port = 0)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
internal class UploadControllerTest {

  @MockkBean
  lateinit var compressFileUseCase: CompressFileUseCase

  @Autowired
  private lateinit var wireMockServer: WireMockServer

  @Autowired
  private lateinit var webClient: WebTestClient

  @BeforeEach
  fun before() {
    mockGetJpg(wireMockServer, "/uploads/image.png", "image.png")
  }

  @Test
  fun `uploaded file is compressed and cached`() {
    val compressedBytes = "Compressed".toByteArray()
    coEvery { compressFileUseCase.compress(any(), any()) } returns compressedBytes

    val actual = webClient.get().uri("/uploads/image.png")
      .exchange()
      .expectStatus().isOk
      .expectBody().returnResult().responseBody

    assertThat(actual).isEqualTo(compressedBytes)

    val cached = webClient.get().uri("/uploads/image.png")
      .exchange()
      .expectStatus().isOk
      .expectBody().returnResult().responseBody
    assertThat(cached).isEqualTo(compressedBytes)

    coVerify(exactly = 1) { compressFileUseCase.compress(any(), any()) }
  }

}
