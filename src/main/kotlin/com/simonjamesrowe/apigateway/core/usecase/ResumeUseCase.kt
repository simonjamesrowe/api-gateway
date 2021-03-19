package com.simonjamesrowe.apigateway.core.usecase

import com.simonjamesrowe.apigateway.core.repository.ResumeRepository
import kotlinx.coroutines.*
import org.springframework.stereotype.Service

@Service
class ResumeUseCase(
  private val resumeRepository: ResumeRepository
) {

  val scope = CoroutineScope(Job() + Dispatchers.IO)
  lateinit var resume: ByteArray

  init {
    scope.launch {
      regenerateResume()
    }
  }

  suspend fun getResume(): ByteArray {
    while (!::resume.isInitialized) {
      delay(100)
    }
    return resume
  }

  suspend fun regenerateResume() {
    val data = resumeRepository.getResumeData()
    resume = ResumeGenerator.generate(data)
  }
}
