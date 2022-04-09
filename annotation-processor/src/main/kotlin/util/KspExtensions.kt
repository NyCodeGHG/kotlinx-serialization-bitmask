package dev.nycode.kotlinx.serialization.bitmask.util

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.asClassName

@KspExperimental
inline fun <reified T : Annotation> KSAnnotated.isAnnotationPresent(): Boolean {
    return isAnnotationPresent(T::class)
}

inline fun <reified T> FileSpec.Builder.addImport(): FileSpec.Builder {
    val className = T::class.asClassName()
    addImport(className.packageName, className.simpleName)
    return this
}
