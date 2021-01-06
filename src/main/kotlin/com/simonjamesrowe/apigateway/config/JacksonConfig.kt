package com.simonjamesrowe.apigateway.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Configuration
class JacksonConfig {

  companion object {
    fun dateTimeModule() : Module {
      val bean = SimpleModule()
      bean.addDeserializer(ZonedDateTime::class.java, object : JsonDeserializer<ZonedDateTime>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ZonedDateTime {
          return ZonedDateTime.parse(jsonParser.valueAsString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        }
      })
      bean.addDeserializer(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime>() {
        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDateTime {
          return LocalDateTime.parse(jsonParser.getValueAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
      })
      bean.addSerializer(ZonedDateTime::class.java, object : JsonSerializer<ZonedDateTime>() {
        @Throws(IOException::class)
        override fun serialize(
          zonedDateTime: ZonedDateTime, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider
        ) {
          jsonGenerator.writeString(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zonedDateTime))
        }
      })
      bean.addSerializer(LocalDateTime::class.java, object : JsonSerializer<LocalDateTime>() {
        @Throws(IOException::class)
        override fun serialize(
          localDateTime: LocalDateTime, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider
        ) {
          jsonGenerator.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime))
        }
      })
      return bean
    }
  }


  @Bean
  fun jsonMapperJava8DateTimeModule() = dateTimeModule()

}