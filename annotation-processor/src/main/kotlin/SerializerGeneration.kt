package dev.nycode.kotlinx.serialization.bitmask

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.toClassName
import dev.nycode.kotlinx.serialization.bitmask.util.addImport
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

private val KSerializer = ClassName("kotlinx.serialization", "KSerializer")

@OptIn(KotlinPoetKspPreview::class)
fun generateSerializer(classDeclaration: KSClassDeclaration): FileSpec {
    val className = classDeclaration.toClassName()
    val name = className.simpleName
    val serializer = className.peerClass("${name}Serializer")

    val descriptor = PropertySpec
        .builder(
            "descriptor",
            SerialDescriptor::class,
            KModifier.OVERRIDE
        )
        .initializer("""PrimitiveSerialDescriptor("$name", PrimitiveKind.INT)""")
        .build()

    val deserialize = generateDeserializeFunction(className, classDeclaration)

    val serialize = generateSerializeFunction(className, classDeclaration)

    return FileSpec.builder(serializer.packageName, name)
        .addImport("kotlinx.serialization.descriptors", "PrimitiveSerialDescriptor")
        .addImport<PrimitiveKind>()
        .addImport("dev.nycode.kotlinx.serialization.bitmask.runtime", "toBoolean")
        .addImport("dev.nycode.kotlinx.serialization.bitmask.runtime", "toInt")
        .addImport("dev.nycode.kotlinx.serialization.bitmask.runtime", "InternalBitmaskApi")
        .addType(
            TypeSpec.classBuilder(serializer)
                .addSuperinterface(KSerializer.parameterizedBy(className))
                .addProperty(descriptor)
                .addFunction(deserialize)
                .addFunction(serialize)
                .build()
        )
        .build()
}

private fun generateSerializeFunction(
    className: ClassName,
    classDeclaration: KSClassDeclaration
) = FunSpec
    .builder("serialize")
    .addModifiers(KModifier.OVERRIDE)
    .addParameter(ParameterSpec("encoder", Encoder::class.asTypeName()))
    .addParameter(ParameterSpec("value", className))
    .addOptInAnnotation()
    .apply {
        for (property in classDeclaration.getAllProperties()) {
            val propertyName = property.simpleName.asString()
            addStatement("val ${propertyName}Bit = value.${propertyName}.toInt()")
        }
        val expression = classDeclaration.getAllProperties().mapIndexed { index, declaration ->
            val propertyName = "${declaration.simpleName.asString()}Bit"
            if (index == 0) {
                propertyName
            } else {
                "(${propertyName} shl $index)"
            }
        }.joinToString(separator = " or ")
        addStatement("val mask = $expression")
        addStatement("encoder.encodeInt(mask)")
    }
    .build()

private fun FunSpec.Builder.addOptInAnnotation(): FunSpec.Builder = addAnnotation(
    AnnotationSpec.builder(ClassName("kotlin", "OptIn"))
        .addMember("InternalBitmaskApi::class")
        .build()
)

private fun generateDeserializeFunction(
    className: ClassName,
    classDeclaration: KSClassDeclaration
) = FunSpec
    .builder("deserialize")
    .addModifiers(KModifier.OVERRIDE)
    .returns(className)
    .addParameter(ParameterSpec("decoder", Decoder::class.asTypeName()))
    .addStatement("val mask = decoder.decodeInt()")
    .addOptInAnnotation()
    .apply {
        val variableNames =
            classDeclaration.getAllProperties().map { "${it.simpleName.asString()}Bit" }
                .toList()
        classDeclaration.getAllProperties().mapIndexed { index, declaration ->
            val expression = if (index == 0) {
                "mask and 1"
            } else {
                "mask shr $index and 1"
            }
            "val ${declaration.simpleName.asString()}Bit = $expression"
        }.forEach {
            addStatement(it)
        }
        addStatement(
            "return %T(${variableNames.joinToString { "$it.toBoolean()" }})",
            className
        )
    }
    .build()
