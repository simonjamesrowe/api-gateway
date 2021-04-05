package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import com.simonjamesrowe.apigateway.core.usecase.IResumeUseCase
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResumeController(
  private val resumeUseCase: IResumeUseCase
) {

  @GetMapping("/resume")
  suspend fun resume(): ResponseEntity<ByteArray> {
    val bytes = resumeUseCase.getResume()
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType("application/pdf"))
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Simon_Rowe_Resume.pdf\"")
      .body(bytes);
  }
}
