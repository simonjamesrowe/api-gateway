package com.simonjamesrowe.apigateway.adapter.driven

import com.simonjamesrowe.model.data.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "cmsRestApi", url = "\${cms.url}")
interface CmsRestApi {

  @GetMapping("/jobs")
  fun getAllJobs(): List<Job>

  @GetMapping("/skills")
  fun getAllSkills(): List<Skill>

  @GetMapping("/social-medias")
  fun getAllSocialMedias(): List<SocialMedia>

  @GetMapping("/profiles")
  fun getProfiles(): List<Profile>

}
