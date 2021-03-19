package com.simonjamesrowe.apigateway

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.springframework.core.io.ClassPathResource
import java.nio.file.Files
import java.util.stream.Collectors

object TestUtils {

  fun mockGet(wireMockServer: WireMockServer, uri: String, responseBodyFile: String) {
    wireMockServer.addStubMapping(
      WireMock.stubFor(
        WireMock.get(WireMock.urlEqualTo(uri))
          .willReturn(
            WireMock.aResponse()
              .withHeader("Content-Type", "application/json")
              .withBody(
                Files.lines(
                  ClassPathResource(responseBodyFile).file.toPath()
                ).collect(Collectors.joining(System.lineSeparator()))
              )
          )
      )
    )
  }

  inline fun <reified T> randomObject(args: Map<String, Any> = mapOf()): T {
    var parameters = EasyRandomParameters()
    args.forEach { param ->
      parameters = parameters.randomize({ it.name == param.key }, { param.value })
    }

    return EasyRandom(parameters).nextObject(T::class.java)
  }

}
