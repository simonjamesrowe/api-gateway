package com.simonjamesrowe.apigateway.dataproviders.cms

import com.simonjamesrowe.model.cms.dto.*
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.sleuth.annotation.NewSpan
import org.springframework.cloud.sleuth.annotation.SpanTag
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class CmsRestApi(
  private val webClient: WebClient,
  @Value("\${cms.url}")
  private val cmsUrl: String
) : ICmsRestApi {


  @NewSpan("http-getAllJobs-cms")
  override suspend fun getAllJobs(): List<JobResponseDTO> = webClient.get()
    .uri("$cmsUrl/jobs")
    .accept(MediaType.APPLICATION_JSON)
    .retrieve()
    .bodyToMono(object : ParameterizedTypeReference<List<JobResponseDTO>>() {})
    .awaitFirst()


  @NewSpan("http-getAllSkillsGroups-cms")
  override suspend fun getAllSkills(): List<SkillResponseDTO> =
    webClient.get()
      .uri("$cmsUrl/skills")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<SkillResponseDTO>>() {})
      .awaitFirst()

  @NewSpan("http-getProfiles-cms")
  override suspend fun getProfiles(): List<ProfileResponseDTO> =
    webClient.get()
      .uri("$cmsUrl/profiles")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<ProfileResponseDTO>>() {})
      .awaitFirst()

  @NewSpan("http-getSocialMedias-cms")
  override suspend fun getAllSocialMedias(): List<SocialMediaRepsonseDTO> =
    webClient.get()
      .uri("$cmsUrl/social-medias")
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(object : ParameterizedTypeReference<List<SocialMediaRepsonseDTO>>() {})
      .awaitFirst()
  
}
