package com.simonjamesrowe.apigateway.core.model

import java.time.LocalDate

data class ResumeData(
  val name: String,
  val phone: String,
  val email: String,
  val headline: String,
  val skills: List<Skill>,
  val jobs: List<Job>,
  val education: List<Job>,
  val links: List<Link>,
) {
  data class Skill(
    val name: String,
    val rating: Double
  )

  data class Job(
    val role: String,
    val company: String,
    val link: String,
    val start: LocalDate,
    val end: LocalDate?,
    val shortDescription: String,
    val location: String
  )

  data class Link(
    val url: String
  )

}
