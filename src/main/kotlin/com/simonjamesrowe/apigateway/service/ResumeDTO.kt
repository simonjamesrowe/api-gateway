package com.simonjamesrowe.apigateway.service

import com.simonjamesrowe.model.data.Job
import com.simonjamesrowe.model.data.Profile
import com.simonjamesrowe.model.data.Skill
import com.simonjamesrowe.model.data.SocialMedia

data class ResumeDTO(
  val jobs: List<Job>,
  val skills: List<Skill>,
  val socialMedias: List<SocialMedia>,
  val profile: Profile,
  val education: Job?
)
