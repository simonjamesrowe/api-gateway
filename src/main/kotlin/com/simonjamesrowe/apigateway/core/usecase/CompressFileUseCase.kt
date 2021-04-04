package com.simonjamesrowe.apigateway.core.usecase

import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.sleuth.annotation.NewSpan
import org.springframework.stereotype.Service
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

@Service
class CompressFileUseCase(
  @Value("\${image.compressFilesLargerThanKb}") private val compressionThreshold: Int
) : ICompressFileUseCase {

  @NewSpan("compressFile")
  override suspend fun compress(file: File, size: Int): ByteArray? {
    if (size < 1024 * compressionThreshold) {
      return IOUtils.toByteArray(FileInputStream(file))
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
    return compressedBytes.toByteArray()
  }
}
