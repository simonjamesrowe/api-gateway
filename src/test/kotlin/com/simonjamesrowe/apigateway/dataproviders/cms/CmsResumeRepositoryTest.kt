package com.simonjamesrowe.apigateway.dataproviders.cms

import com.github.tomakehurst.wiremock.WireMockServer
import com.simonjamesrowe.apigateway.TestUtils.mockGet
import com.simonjamesrowe.apigateway.config.FeignConfig
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@JsonTest
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("cms")
@ImportAutoConfiguration(FeignAutoConfiguration::class, FeignClientsConfiguration::class)
@Import(FeignConfig::class, CmsResumeRepository::class)
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
  fun `should return correct resume data`() = runBlocking {
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
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("link", "https://www.simonjamesrowe.com/jobs/5eedd4803c8d74001e4497f5")
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("start", LocalDate.parse("2020-05-04"))
    assertThat(result.jobs[0]).hasFieldOrPropertyWithValue("location", "London")
    assertThat(result.education).hasSize(1)
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("degree", "Computer Science")
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("university", "University of Newcastle")
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("shortDescription", "Bachelor of Computer Science")
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("start", LocalDate.parse("2002-01-01"))
    assertThat(result.education[0]).hasFieldOrPropertyWithValue("location", "Newcastle")
    assertThat(result.links).hasSize(4)

    println(result)
  }


}
