package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.NamespacedDeclaration

object TypescriptNamespaceRenderer {

    fun NamespacedDeclaration.render(options: DeclarationGeneratorOptions): List<String> {

        return listOfNotNull(
            TypescriptCommentRenderer.renderComment(
                listOfNotNull(
                    documentation?.text,
                    documentation?.url?.let { "@url $it" },
                    documentation?.since?.let { "@since ${it.full()}" }
                ),
                options
            ),
            "export declare namespace $namespace {",
            declarations.map {
                TypescriptRenderer.render(it, options, true).joinToString(options.lineBreakChar)
            }.filter {
                it.isNotBlank()
            }.joinToString(options.lineBreakChar.repeat(options.declarationPadding + 1))
                .split(options.lineBreakChar).joinToString(options.lineBreakChar) {
                    if (it.isNotBlank()) {
                        " ".repeat(options.lineIntend) + it
                    } else it
                },
            "}"
        )
    }
}
