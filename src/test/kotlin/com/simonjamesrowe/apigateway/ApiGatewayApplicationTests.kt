package com.simonjamesrowe.apigateway

import com.simonjamesrowe.component.test.BaseComponentTest
import com.simonjamesrowe.component.test.kafka.WithKafkaContainer
import org.junit.jupiter.api.Test

@WithKafkaContainer
class ApiGatewayApplicationTests : BaseComponentTest() {

	@Test
	fun contextLoads() {
	}

}
