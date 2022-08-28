package io.github.xtoolkit.nyn.interfaces

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class InOut(val input: String, val output: String)
