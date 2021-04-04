package com.simonjamesrowe.apigateway.dataproviders.cms

import com.simonjamesrowe.apigateway.core.model.ResumeData
import com.simonjamesrowe.apigateway.core.repository.ResumeRepository
import com.simonjamesrowe.model.cms.dto.JobResponseDTO
import com.simonjamesrowe.model.cms.dto.SkillResponseDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.cloud.sleuth.annotation.NewSpan
import org.springframework.stereotype.Repository

@Repository
class CmsResumeRepository(
  private val cmsRestApi: CmsRestApi
) : ResumeRepository {

  @NewSpan("getResumeData")
  override suspend fun getResumeData(): ResumeData = coroutineScope {
    val deferredJobs = async(Dispatchers.IO) { cmsRestApi.getAllJobs().filter { it.includeOnResume } }
    val deferredProfile = async(Dispatchers.IO) { cmsRestApi.getProfiles()[0] }
    val deferredSocialMedia = async(Dispatchers.IO) { cmsRestApi.getAllSocialMedias().filter { it.includeOnResume } }
    val deferredSkills =
      async(Dispatchers.IO) { cmsRestApi.getAllSkills().filter { it.includeOnResume }.sortedBy { it.order } }

    val profile = deferredProfile.await()
    val jobs = deferredJobs.await()

    ResumeData(
      name = profile.name,
      phone = profile.phoneNumber,
      email = profile.primaryEmail,
      headline = profile.headline,
      skills = deferredSkills.await().map(::toSkill),
      jobs = jobs.filter { !it.education }.sortedByDescending { it.startDate }.map(::toJob),
      education = jobs.filter { it.education }.map(::toJob),
      links = mutableListOf(ResumeData.Link("www.simonjamesrowe.com")) +
        deferredSocialMedia.await().filter { it.includeOnResume }.map { ResumeData.Link(it.link) }
    )
  }

  private fun toSkill(skillResponseDTO: SkillResponseDTO) = ResumeData.Skill(
    name = skillResponseDTO.name,
    rating = skillResponseDTO.rating
  )

  fun toJob(response: JobResponseDTO) = ResumeData.Job(
    role = response.title,
    company = response.company,
    start = response.startDate,
    end = response.endDate,
    shortDescription = response.shortDescription,
    link = "https://www.simonjamesrowe.com/jobs/${response.id}",
    location = response.location
  )

}
