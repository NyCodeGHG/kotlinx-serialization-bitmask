package dev.nycode.kotlinx.serialization.bitmask

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSTopDownVisitor
import com.squareup.kotlinpoet.ksp.KotlinPoetKspPreview
import com.squareup.kotlinpoet.ksp.writeTo
import dev.nycode.kotlinx.serialization.bitmask.util.isAnnotationPresent

class BitmaskProcessor(private val codeGenerator: CodeGenerator, private val logger: KSPLogger) :
    SymbolProcessor {

    private var invoked = false

    @OptIn(KotlinPoetKspPreview::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (invoked) {
            return emptyList()
        }
        val visitor = ClassVisitor(logger)
        val files = mutableListOf<ProcessedFile>()
        resolver.getAllFiles().forEach {
            it.accept(visitor, files)
        }
        invoked = true
        for ((file, source) in files) {
            file.writeTo(codeGenerator, Dependencies(false, source))
        }
        return emptyList()
    }
}

class ClassVisitor(private val logger: KSPLogger) :
    KSTopDownVisitor<MutableList<ProcessedFile>, Unit>() {
    override fun defaultHandler(node: KSNode, data: MutableList<ProcessedFile>) {
    }

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration,
        data: MutableList<ProcessedFile>,
    ) {
        super.visitClassDeclaration(classDeclaration, data)
        if (classDeclaration.isAnnotationPresent<SerializableBitmask>()) {
            logger.info("Generating serializer for class $classDeclaration")
            val file = classDeclaration.containingFile!!
            val serializer = generateSerializer(classDeclaration)
            data.add(ProcessedFile(serializer, file))
        }
    }
}
