package io.github.xtoolkit.nyn.processor

import io.github.xtoolkit.nyn.interfaces.Flow
import io.github.xtoolkit.nyn.interfaces.InOut
import io.github.xtoolkit.nyn.processor.model.FlowInfo
import io.github.xtoolkit.nyn.processor.model.Method
import io.github.xtoolkit.nyn.processor.template.flowTemplate
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(CodeGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class CodeGenerator : AbstractProcessor() {
    private val flow = Flow::class.java
    private val inOut = InOut::class.java

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(flow.canonicalName, inOut.canonicalName)
    }

    override fun process(p0: MutableSet<out TypeElement>?, p1: RoundEnvironment?): Boolean {
        val methods = mutableListOf<Method>();
        p1?.getElementsAnnotatedWith(inOut)?.forEach {
            val annotationArgs = it.getAnnotation(inOut)
            val methodType = getMethodType(it)
            methods.add(
                Method(
                    annotationArgs.input,
                    annotationArgs.output,
                    processingEnv.elementUtils.getPackageOf(it).toString(),
                    it.enclosingElement.simpleName.toString(),
                    it.simpleName.toString(),
                    methodType.first,
                    methodType.second
                )
            )
        }
        checkMethodsType(methods)
        p1?.getElementsAnnotatedWith(flow)?.forEach {
            val annotationArgs = it.getAnnotation(flow)
            create(
                FlowInfo(
                    annotationArgs.direction.toList(),
                    processingEnv.elementUtils.getPackageOf(it).toString(),
                    it.simpleName.toString()
                ),
                methods.filter { method -> annotationArgs.direction.find { x -> x == method.input } != null }
            )
        }
        return true
    }

    private fun create(flowInfo: FlowInfo, methods: List<Method>) =
        File(
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME],
            "${flowInfo.className}Choop.kt"
        ).writeText(
            flowTemplate(flowInfo, methods)
        )

    private fun getMethodType(element: Element): Pair<String, String> {
        val type = element.asType().toString()
        var arg = ""
        var returnType = ""

        val search = Regex("\\((?<arg>.*)\\)(?<return>.*)")
        search.matchEntire(type)?.groupValues?.let {
            arg = it[1]
            returnType = it[2]
        }

        return arg to returnType
    }

    private fun checkMethodsType(methods: List<Method>) = methods.forEach {
        methods.find { method -> method.input == it.output }?.let { item ->
            if (item.arg != it.returnType) {
                throw Exception("${it.output} in ${item.packageName}.${item.className}.${item.methodName} Types not match!")
            }
        }
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}
