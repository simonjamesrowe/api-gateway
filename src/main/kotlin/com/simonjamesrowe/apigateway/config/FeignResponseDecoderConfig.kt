package com.simonjamesrowe.apigateway.config

import feign.codec.Decoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignResponseDecoderConfig {
  @Bean
  fun feignDecoder(): Decoder {
    val messageConverters: ObjectFactory<HttpMessageConverters> =
      ObjectFactory<HttpMessageConverters> {
        val converters =
          HttpMessageConverters()
        converters
      }
    return SpringDecoder(messageConverters)
  }
}
