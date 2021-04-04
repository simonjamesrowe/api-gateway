package com.simonjamesrowe.apigateway.dataproviders.cms

import com.simonjamesrowe.model.cms.dto.JobResponseDTO
import com.simonjamesrowe.model.cms.dto.ProfileResponseDTO
import com.simonjamesrowe.model.cms.dto.SkillResponseDTO
import com.simonjamesrowe.model.cms.dto.SocialMediaRepsonseDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.sleuth.annotation.NewSpan
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "cmsRestApi", url = "\${cms.url}")
interface CmsRestApi {

  @NewSpan("cms-getJobs")
  @GetMapping("/jobs")
  fun getAllJobs(): List<JobResponseDTO>

  @NewSpan("cms-getSkills")
  @GetMapping("/skills")
  fun getAllSkills(): List<SkillResponseDTO>

  @NewSpan("cms-getSocialMedias")
  @GetMapping("/social-medias")
  fun getAllSocialMedias(): List<SocialMediaRepsonseDTO>

  @NewSpan("cms-getProfiles")
  @GetMapping("/profiles")
  fun getProfiles(): List<ProfileResponseDTO>

}
