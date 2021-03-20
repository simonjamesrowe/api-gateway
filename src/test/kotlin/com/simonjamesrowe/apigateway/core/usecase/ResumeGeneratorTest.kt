package com.simonjamesrowe.apigateway.core.usecase

import com.simonjamesrowe.apigateway.core.model.ResumeData
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.time.LocalDate

internal class ResumeGeneratorTest {

  @Test
  fun `should generate expected pdf byte array`() = runBlocking<Unit> {
    val testData = ResumeData(
      name = "Test McTest",
      email = "test@gmail.com",
      phone = "+4478923234234",
      headline = "Cloud Native Developer & Architect",
      education = listOf(
        ResumeData.Job(
          role = "Computer Science",
          company = "University of Newcastle",
          shortDescription = "Bachelor of Computer Science WAM 82",
          location = "Newcastle",
          start = LocalDate.parse("2002-01-01"),
          end = LocalDate.parse("2004-12-31"),
          link = "http://link"
        )
      ),
      jobs = listOf(
        ResumeData.Job(
          role = "Principal Developer",
          company = "Company 1",
          shortDescription = "Doing principal developer things",
          location = "London",
          start = LocalDate.parse("2021-01-01"),
          end = null,
          link = "http://job1"
        ),
        ResumeData.Job(
          role = "Senior Developer Developer",
          company = "Company 2",
          shortDescription = "Doing Senior Developer Things",
          location = "London",
          start = LocalDate.parse("2020-01-01"),
          end = LocalDate.parse("2020-12-31"),
          link = "http://job1"
        )
      ),
      links = listOf(ResumeData.Link("link1"), ResumeData.Link("link2")),
      skills = listOf(
        ResumeData.Skill(
          name = "Java",
          rating = 6.5
        ),
        ResumeData.Skill(
          name = "Kotlin",
          rating = 9.0
        )
      )
    )
    val result = ResumeGenerator.generate(testData)
    val expected = Files.readAllBytes(ClassPathResource("resume.pdf").file.toPath())
    assertThat(result.size).isEqualTo(expected.size)
  }
}
