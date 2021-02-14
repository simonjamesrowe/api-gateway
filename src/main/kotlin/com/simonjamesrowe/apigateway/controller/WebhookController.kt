package com.simonjamesrowe.apigateway.controller

import com.simonjamesrowe.model.data.Event
import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WebhookController(
  private val streamBridge: StreamBridge
) {

  @PostMapping("/webhook")
  fun webhookPost(
    @RequestBody event: Event
  ) {
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
