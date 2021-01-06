package com.simonjamesrowe.apigateway.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SecurityConfig {

  @Bean
  fun securityWebFilterChain(
    http: ServerHttpSecurity
  ): SecurityWebFilterChain {
    var builder = http.csrf().disable().cors().disable().httpBasic().and()
    builder = secureWebhooks(builder)
    builder = configureOpenEndpoints(builder)
    return builder.build()
  }

  private fun secureWebhooks(builder: ServerHttpSecurity) =
    builder.authorizeExchange().pathMatchers("/webhook/**").authenticated().and()

  private fun configureOpenEndpoints(builder: ServerHttpSecurity) =
    builder.authorizeExchange().pathMatchers("/**").permitAll().and()

}