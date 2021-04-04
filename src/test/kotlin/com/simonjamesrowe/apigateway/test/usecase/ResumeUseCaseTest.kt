package com.simonjamesrowe.apigateway.test.usecase

import com.simonjamesrowe.apigateway.test.TestUtils.randomObject
import com.simonjamesrowe.apigateway.core.model.ResumeData
import com.simonjamesrowe.apigateway.core.repository.ResumeRepository
import com.simonjamesrowe.apigateway.core.usecase.ResumeGenerator
import com.simonjamesrowe.apigateway.core.usecase.ResumeUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ResumeUseCaseTest {

  @RelaxedMockK
  lateinit var resumeRepository: ResumeRepository

  @BeforeEach
  fun before() {
    mockkObject(ResumeGenerator)
  }

  @AfterEach
  fun after() {
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun `should initialize resume data in constructor`() = runBlocking<Unit> {
    val testData = randomObject<ResumeData>()
    coEvery { resumeRepository.getResumeData() } returns testData
    val bytes = "initial resume bytes".toByteArray()
    coEvery { ResumeGenerator.generate(testData) } returns bytes
    assertThat(ResumeUseCase(resumeRepository).getResume()).isEqualTo(bytes)
  }

  @Test
  fun `should regenerate resume`() = runBlocking<Unit> {
    val testData = randomObject<ResumeData>()
    coEvery { resumeRepository.getResumeData() } returns testData
    val bytes = "initial resume bytes".toByteArray()
    coEvery { ResumeGenerator.generate(testData) } returns bytes
    val useCase = ResumeUseCase(resumeRepository)
    val newTestData =  randomObject<ResumeData>()
    val newBytes = "new resume bytes".toByteArray()
    coEvery { ResumeGenerator.generate(newTestData) } returns newBytes
    useCase.regenerateResume()
    assertThat(useCase.getResume()).isEqualTo(newBytes)
  }
}
