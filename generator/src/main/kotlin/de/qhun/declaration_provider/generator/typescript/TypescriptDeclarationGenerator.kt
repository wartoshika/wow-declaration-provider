package de.qhun.declaration_provider.generator.typescript

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.domain.helper.mapChunked
import de.qhun.declaration_provider.domain.helper.sortByField
import de.qhun.declaration_provider.generator.DeclarationGenerator
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.render.RenderResult
import de.qhun.declaration_provider.generator.typescript.render.TypescriptRenderer
import kotlinx.coroutines.coroutineScope
import kotlin.reflect.KFunction

@Suppress("unused")
object TypescriptDeclarationGenerator : DeclarationGenerator {

    override suspend fun generate(declarations: List<Declaration>, options: DeclarationGeneratorOptions) =
        coroutineScope {

            declarations
                .separateNamespaced()
                .renderDeclaration(options)
                .sortAndReduce(options)
        }

    private fun List<Declaration>.separateNamespaced(): List<Declaration> {

        val namespaced = mutableMapOf<String, MutableList<Declaration>>()
        val others = mutableListOf<Declaration>()
        forEach {
            if (it.namespace != null) {
                if (namespaced[it.namespace] == null) {
                    namespaced[it.namespace!!] = mutableListOf()
                }
                namespaced[it.namespace]!!.add(it)
            } else {
                others.add(it)
            }
        }
        return listOf(
            *namespaced.map {
                NamespacedDeclaration(
                    name = it.key,
                    score = 50,
                    documentation = null,
                    deprecated = false,
                    namespace = it.key,
                    declarations = it.value.sortByField(Declaration::name)
                )
            }.toTypedArray(),
            *others.toTypedArray()
        )
    }

    private fun List<Declaration>.renderDeclaration(options: DeclarationGeneratorOptions): List<RenderResult> {

        return map {
            val codeLines = TypescriptRenderer.render(it, options, it.namespace != null)
            RenderResult(
                fileName = it.namespace ?: "global",
                declarationSortableName = it.name,
                code = codeLines.joinToString(options.lineBreakChar)
            )
        }
    }


    private fun List<RenderResult>.sortAndReduce(options: DeclarationGeneratorOptions): Map<String, String> {

        val map = mutableMapOf<String, MutableList<String>>()

        sortByField(RenderResult::declarationSortableName).forEach {
            if (map[it.fileName] == null) {
                map[it.fileName] = mutableListOf()
            }
            if (it.code.isNotBlank()) {
                map[it.fileName]!!.add(it.code)
            }
        }

        return map.mapValues { it.value.joinToString(options.lineBreakChar.repeat(options.declarationPadding + 1)) }
    }
}
