package com.simonjamesrowe.apigateway.core.usecase

import com.simonjamesrowe.apigateway.core.repository.ResumeRepository
import kotlinx.coroutines.*

class ResumeUseCase(
  private val resumeRepository: ResumeRepository
) {
  val scope = CoroutineScope(Job() + Dispatchers.IO)

  init {
    scope.launch {
      regenerateResume()
    }
  }

  suspend fun getResume(): ByteArray {
    return ByteArray(0)
  }

  suspend fun regenerateResume() {

  }
}
