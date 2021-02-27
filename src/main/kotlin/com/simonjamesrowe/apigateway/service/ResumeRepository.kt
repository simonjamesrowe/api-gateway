package com.simonjamesrowe.apigateway.service

interface ResumeRepository {
  fun getResumeData() : ResumeDTO
}
