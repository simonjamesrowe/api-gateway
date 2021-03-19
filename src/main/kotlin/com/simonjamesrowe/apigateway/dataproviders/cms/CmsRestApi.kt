package com.simonjamesrowe.apigateway.dataproviders.cms

import com.simonjamesrowe.model.cms.dto.JobResponseDTO
import com.simonjamesrowe.model.cms.dto.ProfileResponseDTO
import com.simonjamesrowe.model.cms.dto.SkillResponseDTO
import com.simonjamesrowe.model.cms.dto.SocialMediaRepsonseDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "cmsRestApi", url = "\${cms.url}")
interface CmsRestApi {

  @GetMapping("/jobs")
  fun getAllJobs(): List<JobResponseDTO>

  @GetMapping("/skills")
  fun getAllSkills(): List<SkillResponseDTO>

  @GetMapping("/social-medias")
  fun getAllSocialMedias(): List<SocialMediaRepsonseDTO>

  @GetMapping("/profiles")
  fun getProfiles(): List<ProfileResponseDTO>

}
