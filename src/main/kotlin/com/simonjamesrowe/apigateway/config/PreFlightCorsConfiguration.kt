package com.simonjamesrowe.apigateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
class PreFlightCorsConfiguration {

  @Bean
  fun corsFilter(): CorsWebFilter {
    return CorsWebFilter(corsConfigurationSource())
  }

  private fun corsConfigurationSource(): CorsConfigurationSource {
    val source = UrlBasedCorsConfigurationSource()
    val config = CorsConfiguration().applyPermitDefaultValues()
    config.addAllowedMethod(HttpMethod.PUT)
    config.addAllowedMethod(HttpMethod.PATCH)
    config.addAllowedMethod(HttpMethod.DELETE)
    source.registerCorsConfiguration("/**", config)
    return source
  }

}
