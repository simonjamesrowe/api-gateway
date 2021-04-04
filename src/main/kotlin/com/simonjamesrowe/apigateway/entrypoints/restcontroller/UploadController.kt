package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import com.simonjamesrowe.apigateway.core.usecase.CompressFileUseCase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.withContext
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.webflux.ProxyExchange
import org.springframework.cloud.sleuth.annotation.NewSpan
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.*
import javax.imageio.ImageIO


@RestController
class UploadController(
  @Value("\${cms.url}") private val cmsUrl: String,
  private val compressFileUseCase: CompressFileUseCase
) : IUploadController {
  companion object {
    val logger = LoggerFactory.getLogger(UploadController::class.java)
    val imageCache = HashMap<String, ByteArray?>()
    val imageHeadersCache = HashMap<String, HttpHeaders>()
    val tmpDir: String = System.getProperty("java.io.tmpdir")
  }

  @GetMapping("/uploads/{file}")
  @NewSpan("GET /uploads/{file}")
  override suspend fun proxy(
    @PathVariable file: String,
    @RequestHeader headers: HttpHeaders,
    proxy: ProxyExchange<ByteArray?>
  ): ResponseEntity<ByteArray?>? {
    if (!isImage(file)) {
      return proxy.uri("${cmsUrl}uploads/$file").get().awaitFirst()
    }

    logger.debug("Request for file has been made $file")
    logger.debug("headers are $headers")

    if (imageCache.containsKey(file)) {
      logger.debug("Image is in the cache!")
      return ResponseEntity(imageCache[file], imageHeadersCache[file], HttpStatus.OK) as ResponseEntity<ByteArray?>
    }

    return proxy.uri("${cmsUrl}uploads/$file").get().awaitFirst().let { response ->
      withContext(IO) {
        var tmpFile = File(tmpDir, file)
        IOUtils.copyLarge(ByteArrayInputStream(response.body), FileOutputStream(tmpFile))
        imageCache[file] = compressFileUseCase.compress(tmpFile, response.body!!.size)
        imageHeadersCache[file] = response.headers
      }
      ResponseEntity(imageCache[file], imageHeadersCache[file], HttpStatus.OK) as ResponseEntity<ByteArray?>
    }
  }

  private fun isImage(file: String) = file.endsWith(".jpg") || file.endsWith(".png")


}
