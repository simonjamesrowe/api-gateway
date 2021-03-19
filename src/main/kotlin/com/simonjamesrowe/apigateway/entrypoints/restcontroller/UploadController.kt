package com.simonjamesrowe.apigateway.entrypoints.restcontroller

import kotlinx.coroutines.reactive.awaitFirst
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.webflux.ProxyExchange
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
  @Value("\${image.compressFilesLargerThanKb}") private val compressionThreshold: Int
) {
  companion object {
    val logger = LoggerFactory.getLogger(UploadController::class.java)
    val imageCache = HashMap<String, ByteArray?>()
    val imageHeadersCache = HashMap<String, HttpHeaders>()
    val tmpDir: String = System.getProperty("java.io.tmpdir")
  }

  @GetMapping("/uploads/{file}")
  suspend fun proxy(
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

    return proxy.uri("${cmsUrl}uploads/$file").get().awaitFirst().run {
      var tmpFile = File(tmpDir, file)
      IOUtils.copyLarge(ByteArrayInputStream(body), FileOutputStream(tmpFile))
      imageCache[file] = compress(tmpFile, body!!.size)
      imageHeadersCache[file] = headers
      ResponseEntity(imageCache[file], imageHeadersCache[file], HttpStatus.OK) as ResponseEntity<ByteArray?>
    }

  }

  private fun isImage(file: String) = file.endsWith(".jpg") || file.endsWith(".png")

  fun compress(file: File, size: Int): ByteArray? {
    if (size < 1024 * compressionThreshold) {
      var byteArray = IOUtils.toByteArray(FileInputStream(file))
      file.delete()
      return byteArray
    }
    // reads input image
    val original = ImageIO.read(file)
    val scaledWidth: Int = ((original.width * .5).toInt())
    val scaledHeight: Int = ((original.height * .5).toInt())
    // creates output image
    val outputImage = BufferedImage(
      scaledWidth,
      scaledHeight, original.type
    )

    // scales the input image to the output image
    val g2d = outputImage.createGraphics()
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.drawImage(original, 0, 0, scaledWidth, scaledHeight, null)
    g2d.dispose()

    var compressedBytes = ByteArrayOutputStream(1024)
    // writes to output file
    ImageIO.write(outputImage, file.name.substring(file.name.indexOf(".") + 1), compressedBytes)
    file.delete()
    return compressedBytes.toByteArray()
  }
}
