package com.simonjamesrowe.apigateway.core.usecase

import org.springframework.cloud.sleuth.annotation.NewSpan

interface IResumeUseCase {
  suspend fun getResume(): ByteArray

  @NewSpan("regenerateResume")
  suspend fun regenerateResume()
}
