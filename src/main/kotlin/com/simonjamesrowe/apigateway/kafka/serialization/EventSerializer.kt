package com.simonjamesrowe.apigateway.kafka.serialization

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.simonjamesrowe.apigateway.config.JacksonConfig
import com.simonjamesrowe.model.Event
import org.springframework.kafka.support.serializer.JsonSerializer

class EventSerializer : JsonSerializer<Event>(
  jacksonObjectMapper().also {
    it.registerModule(JacksonConfig.dateTimeModule())
  }
)
