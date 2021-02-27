package com.simonjamesrowe.apigateway.service

import com.github.tomakehurst.wiremock.client.WireMock
import com.simonjamesrowe.component.test.BaseComponentTest
import com.simonjamesrowe.component.test.kafka.WithKafkaContainer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

@WithKafkaContainer
internal class ResumeInteractorTest : BaseComponentTest() {

  @Autowired
  private lateinit var resumeInteractor: ResumeInteractor

  @BeforeEach
  fun beforeEach() {
    cmsWireMockMapping("/social-medias", "social-media.json")
    cmsWireMockMapping("/jobs", "jobs.json")
    cmsWireMockMapping("/skills", "skills.json")
    cmsWireMockMapping("/profiles", "profile.json")
  }

  private fun cmsWireMockMapping(endpoint: String, bodyFile: String) {
    wireMockServer.addStubMapping(
      WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo(endpoint))
          .willReturn(
            WireMock.aResponse()
              .withHeader("Content-Type", "application/json")
              .withBody(
                Files.lines(
                  ClassPathResource(bodyFile).file.toPath()
                ).collect(Collectors.joining(System.lineSeparator()))
              )
          )
      )
    )
  }

  @Test
  fun `resume should be generated as pdf`() {
    val bytes = resumeInteractor.toPdf()
    assertThat(bytes).isNotNull
  }
}
