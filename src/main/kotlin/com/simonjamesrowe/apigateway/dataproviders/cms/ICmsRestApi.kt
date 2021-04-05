package com.simonjamesrowe.apigateway.dataproviders.cms

import com.simonjamesrowe.model.cms.dto.JobResponseDTO
import com.simonjamesrowe.model.cms.dto.ProfileResponseDTO
import com.simonjamesrowe.model.cms.dto.SkillResponseDTO
import com.simonjamesrowe.model.cms.dto.SocialMediaRepsonseDTO
import org.springframework.cloud.sleuth.annotation.NewSpan

interface ICmsRestApi {

  suspend fun getAllJobs(): List<JobResponseDTO>

  suspend fun getAllSkills(): List<SkillResponseDTO>

  suspend fun getProfiles(): List<ProfileResponseDTO>

  suspend fun getAllSocialMedias(): List<SocialMediaRepsonseDTO>
}
