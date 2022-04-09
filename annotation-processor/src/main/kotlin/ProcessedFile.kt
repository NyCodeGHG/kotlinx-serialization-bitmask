package dev.nycode.kotlinx.serialization.bitmask

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec

data class ProcessedFile(val file: FileSpec, val sourceFile: KSFile)
