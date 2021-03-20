package com.simonjamesrowe.apigateway.core.repository

import com.simonjamesrowe.apigateway.core.model.ResumeData

interface ResumeRepository {

  suspend fun getResumeData(): ResumeData
}
