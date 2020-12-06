package de.qhun.declaration_provider.application

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import kotlinx.coroutines.coroutineScope

object ImplementationProvider {

    var scanResult: ScanResult? = null

    inline fun <reified T> find(predicate: (T) -> Boolean = { true }): List<T> = try {

        if (scanResult == null) {
            scanResult = ClassGraph()
                .enableClassInfo()
                .scan(Runtime.getRuntime().availableProcessors())
        }

        scanResult!!
            .getClassesImplementing(T::class.qualifiedName)
            .map {
                val clazz = it.loadClass()
                val instanceField = clazz.getField("INSTANCE")
                instanceField.get(clazz) as T
            }
            .filter(predicate)

    } catch (e: Throwable) {
        throw RuntimeException(e)
    }

}
