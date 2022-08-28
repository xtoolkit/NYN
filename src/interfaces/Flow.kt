package io.github.xtoolkit.nyn.interfaces

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Flow(val direction: Array<String>)
