package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions

object TypescriptCommentRenderer {

    fun renderComment(lines: List<String>, options: DeclarationGeneratorOptions): String? {

        if (lines.isEmpty()) {
            return null
        }

        return listOf(
            listOf("/**"),
            lines.map { " * $it" },
            listOf(" */")
        ).flatten().joinToString(options.lineBreakChar)
    }
}
