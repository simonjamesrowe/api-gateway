package com.simonjamesrowe.apigateway.test.usecase

import com.simonjamesrowe.apigateway.core.usecase.CompressFileUseCase
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files

internal class CompressFileUseCaseTest {

  private val compressFileUseCase = CompressFileUseCase(1000)

  @Test
  fun `should compress file`() = runBlocking<Unit> {
    val testImage = ClassPathResource("big-image.jpg").file
    val testImageSize = Files.readAllBytes(testImage.toPath()).size

    val compressed = compressFileUseCase.compress(testImage, testImageSize)

    assertThat(compressed!!.size).isLessThan(testImageSize)
  }

}
