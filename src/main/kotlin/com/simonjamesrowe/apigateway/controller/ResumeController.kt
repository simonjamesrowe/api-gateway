package com.simonjamesrowe.apigateway.controller

import com.simonjamesrowe.apigateway.service.ResumeInteractor
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResumeController(
  private val resumeInteractor: ResumeInteractor
) {

  @GetMapping("/resume")
  fun resume() : ResponseEntity<ByteArray>{
    val bytes = resumeInteractor.toPdf()
    return ResponseEntity.ok()
      .contentType(MediaType.parseMediaType("application/pdf"))
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Simon_Rowe_Resume.pdf\"")
      .body(bytes);
  }
}
