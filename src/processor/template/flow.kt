package io.github.xtoolkit.nyn.processor.template

import io.github.xtoolkit.nyn.processor.model.FlowInfo
import io.github.xtoolkit.nyn.processor.model.Method

fun flowTemplate(flowInfo: FlowInfo, methods: List<Method>): String {
    val direction = flowInfo.direction.mapNotNull {
        methods.find { method -> method.input == it }
    }
    return """
            /*
                Simple
            */
            
            package ${flowInfo.packageName}
            
            ${
        direction.joinToString("") { "import ${it.packageName}.${it.className}\n            " }
    }
            class ${flowInfo.className}Choop(
                ${
        direction.joinToString("") { "private val ${it.className.lowercase()}: ${it.className},\n                " }
    }
            ) {
                fun execute() = ${
        direction.asReversed().joinToString("") { "${it.className.lowercase()}.${it.methodName}(" }
    }${direction.joinToString("") { ")" }}
            }
        """.trimIndent()
}
