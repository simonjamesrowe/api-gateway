package com.simonjamesrowe.apigateway.entrypoints.restcontroller


import com.simonjamesrowe.apigateway.core.usecase.ResumeUseCase
import com.simonjamesrowe.model.cms.dto.Constants.TYPE_JOB
import com.simonjamesrowe.model.cms.dto.Constants.TYPE_SKILL
import com.simonjamesrowe.model.cms.dto.WebhookEventDTO
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController(
  private val streamBridge: StreamBridge,
  private val resumeUseCase: ResumeUseCase
) {

  @PostMapping("/webhook")
  suspend fun webhookPost(
    @RequestBody event: WebhookEventDTO
  ) {
    resumeUseCase.regenerateResume()

    withContext(IO) {
      streamBridge.send(
        "output",
        MessageBuilder
          .withPayload(event)
          .setHeader("model", event.model)
          .setHeader(KafkaHeaders.MESSAGE_KEY, "${event.model}-${event.entry["id"].textValue()}")
          .build()
      )
    }
  }
}
