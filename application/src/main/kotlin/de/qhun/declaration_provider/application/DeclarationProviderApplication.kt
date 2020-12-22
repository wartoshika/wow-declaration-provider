package de.qhun.declaration_provider.application

import de.qhun.declaration_provider.domain.WowVersion
import de.qhun.declaration_provider.generator.DeclarationGenerator
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.provider.DeclarationProvider
import de.qhun.declaration_provider.reducer.DeclarationReducer
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.nio.file.Paths

suspend fun main() = coroutineScope {

    val providers = ImplementationProvider.find<DeclarationProvider>()
    val reducer = ImplementationProvider.find<DeclarationReducer>().firstOrNull()!!
    val generator = ImplementationProvider.find<DeclarationGenerator>().firstOrNull()!!
    val generatorOptions = DeclarationGeneratorOptions()
    val destination = Paths.get("./target")

    val code = providers
        .map {
            println("+ Using ${it::class.simpleName}")
            async { it.provide(WowVersion.RETAIL) }
        }
        .flatMap { it.await() }
        .let {
            println("+ Successfully downloaded ${it.size} declarations")
            async { reducer.reduce(it) }
        }
        .let { asyncList ->
            asyncList.await().let {
                println("+ Reduced declarations to a total of ${it.size}")
                async {
                    println("+ Starting Typescript declaration file generation")
                    generator.generate(it, generatorOptions)
                }
            }
        }.await()

    destination.toAbsolutePath().toFile().also { destDir ->

        // create target directory
        if (!destDir.exists()) {
            destDir.mkdir()
        }

        code.forEach {
            val destFile = File(listOf(destDir.toString(), it.key + ".d.ts").joinToString("/"))
            destFile.writeText(it.value)
        }
        println("+ Generated the following files:")
    }

    Unit
}
