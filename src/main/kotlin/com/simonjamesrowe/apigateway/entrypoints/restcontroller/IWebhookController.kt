package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import com.simonjamesrowe.model.cms.dto.WebhookEventDTO

interface IWebhookController {
  suspend fun webhookPost(
    event: WebhookEventDTO
  )
}
