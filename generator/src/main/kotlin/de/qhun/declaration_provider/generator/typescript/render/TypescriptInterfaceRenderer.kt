package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.InterfaceDeclaration
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions

internal object TypescriptInterfaceRenderer {

    fun InterfaceDeclaration.render(
        options: DeclarationGeneratorOptions,
        namespaceContext: Boolean = false
    ): List<String> {

        return listOfNotNull(
            TypescriptCommentRenderer.renderComment(
                listOfNotNull(
                    documentation?.text,
                    documentation?.url?.let { "@url $it" },
                    documentation?.since?.let { "@since ${it.full()}" }
                ),
                options
            ),
            "${if (namespaceContext) "" else "declare "}interface $name {",
            fields.map {
                TypescriptRenderer.render(it, options, namespaceContext).joinToString(options.lineBreakChar)
            }.filter {
                it.isNotBlank()
            }.joinToString(options.lineBreakChar)
                .split(options.lineBreakChar).joinToString(options.lineBreakChar) {
                    if (it.isNotBlank()) {
                        " ".repeat(options.lineIntend) + it
                    } else it
                },
            "}"
        )
    }
}
