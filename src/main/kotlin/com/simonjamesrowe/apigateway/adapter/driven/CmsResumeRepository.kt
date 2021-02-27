package com.simonjamesrowe.apigateway.adapter.driven

import com.simonjamesrowe.apigateway.service.ResumeDTO
import com.simonjamesrowe.apigateway.service.ResumeRepository
import org.springframework.stereotype.Repository

@Repository
class CmsResumeRepository(
  private val cmsRestApi: CmsRestApi
) : ResumeRepository {
  override fun getResumeData(): ResumeDTO {
    val allJobs = cmsRestApi.getAllJobs()
    return ResumeDTO(
      jobs = allJobs.filter { it.includeOnResume && !it.education }.sortedByDescending { it.startDate },
      skills = cmsRestApi.getAllSkills().filter { it.includeOnResume }.sortedBy { it.order },
      socialMedias = cmsRestApi.getAllSocialMedias().filter { it.includeOnResume },
      profile = cmsRestApi.getProfiles()[0],
      education = allJobs.find { it.includeOnResume && it.education }
    )
  }
}
