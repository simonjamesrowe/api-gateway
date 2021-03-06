package com.simonjamesrowe.apigateway

import co.elastic.apm.attach.ElasticApmAttacher
import co.elastic.apm.opentracing.ElasticApmTracer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableFeignClients
class ApiGatewayApplication {

  @Bean
  fun apmTracer() = ElasticApmTracer()

}

fun main(args: Array<String>) {
  ElasticApmAttacher.attach()
  runApplication<ApiGatewayApplication>(*args)
}
