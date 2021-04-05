package com.simonjamesrowe.apigateway.test.dataproviders.cms

import com.github.tomakehurst.wiremock.WireMockServer
import com.simonjamesrowe.apigateway.config.WebClientConfiguration
import com.simonjamesrowe.apigateway.dataproviders.cms.CmsRestApi
import com.simonjamesrowe.apigateway.dataproviders.cms.CmsResumeRepository
import com.simonjamesrowe.apigateway.test.TestUtils.mockGet
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@JsonTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("cms")
@Import(CmsRestApi::class, CmsResumeRepository::class, WebClientConfiguration::class)
internal class CmsResumeRepositoryTest {

  @Autowired
  lateinit var wireMockServer: WireMockServer

  @Autowired
  lateinit var cmsResumeRepository: CmsResumeRepository

  @BeforeEach
  fun setup() {
    mockGet(wireMockServer, "/profiles", "profile.json")
    mockGet(wireMockServer, "/skills", "skills.json")
    mockGet(wireMockServer, "/jobs", "jobs.json")
    mockGet(wireMockServer, "/social-medias", "social-media.json")
  }

  @Test
  fun `should return correct resume data`() = runBlocking<Unit> {
    val result = cmsResumeRepository.getResumeData()
    assertThat(result.name).isEqualTo("Simon Rowe")
    assertThat(result.phone).isEqualTo("+447909083522")
    assertThat(result.email).isEqualTo("simon.rowe@gmail.com")
    assertThat(result.headline).isEqualTo("PASSIONATE ABOUT BUILDING CLOUD NATIVE APPS UTILIZING SPRING, KAFKA AND KUBERNETES.")
    assertThat(result.skills).hasSize(11)
    assertThat(result.skills[0]).hasFieldOrPropertyWithValue("name", "Java 9-11")
    assertThat(result.skills[0]).hasFieldOrPropertyWithValue("rating", 9.0)
    assertThat(result.jobs).hasSize(6)
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("role", "Senior Developer")
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("company", "Y-Tree")
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue(
      "link",
      "https://www.simonjamesrowe.com/jobs/5eedd4803c8d74001e4497f5"
    )
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("start", LocalDate.parse("2020-05-04"))
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("location", "London")
    assertThat(result.education).hasSize(1)
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("role", "Computer Science")
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("company", "University of Newcastle")
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("shortDescription", "Bachelor of Computer Science")
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("start", LocalDate.parse("2002-01-01"))
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("location", "Newcastle")
    assertThat(result.links).hasSize(4)
  }


}
