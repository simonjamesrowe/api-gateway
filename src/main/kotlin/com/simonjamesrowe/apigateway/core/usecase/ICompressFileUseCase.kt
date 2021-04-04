package com.simonjamesrowe.apigateway.core.usecase

import java.io.File

interface ICompressFileUseCase {
  suspend fun compress(file: File, size: Int): ByteArray?
}
