package io.github.xtoolkit.nyn.processor.model

data class Method(
    val input: String,
    val output: String,
    val packageName: String,
    val className: String,
    val methodName: String,
    val arg: String,
    val returnType: String
)
