package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.EnumDeclaration
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.render.TypescriptConstantRenderer.render

internal object TypescriptEnumRenderer {

    fun EnumDeclaration.render(options: DeclarationGeneratorOptions, namespaceContext: Boolean = false): List<String> {

        return listOfNotNull(
            "${if (namespaceContext) "" else "declare "}enum $name {",
            entries.joinToString("," + options.lineBreakChar) {
                " ".repeat(options.lineIntend) + it.render(options, true, namespaceContext).joinToString("")
            },
            "}"
        )
    }
}
