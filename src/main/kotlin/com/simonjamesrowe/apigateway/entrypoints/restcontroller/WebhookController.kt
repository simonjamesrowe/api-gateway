package com.simonjamesrowe.apigateway.entrypoints.restcontroller


import brave.Tracer
import com.simonjamesrowe.apigateway.core.usecase.ResumeUseCase
import com.simonjamesrowe.model.cms.dto.WebhookEventDTO
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.sleuth.annotation.NewSpan
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController(
  private val kafkaTemplate: KafkaTemplate<String, WebhookEventDTO>,
  private val resumeUseCase: ResumeUseCase,
  @Value("\${namespace:LOCAL}_EVENTS") private val eventTopic: String,
  private val tracer: Tracer
) : IWebhookController {

  @PostMapping("/webhook")
  @NewSpan("POST /webhook")
  override suspend fun webhookPost(
    @RequestBody event: WebhookEventDTO
  ) {
    resumeUseCase.regenerateResume()
    val traceId = tracer.currentSpan().context().traceId()
    withContext(IO) {
      kafkaTemplate.send(
        MessageBuilder
          .withPayload(event)
          .setHeader("model", event.model)
          .setHeader(KafkaHeaders.TOPIC, eventTopic)
          .setHeader("b3", traceId)
          .setHeader(KafkaHeaders.MESSAGE_KEY, "${event.model}-${event.entry["id"].textValue()}")
          .build()
      )
    }
  }
}
