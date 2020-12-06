package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.PropertyDeclaration
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.render.TypescriptDataTypeRenderer.render

internal object TypescriptPropertyRenderer {

    fun PropertyDeclaration.render(options: DeclarationGeneratorOptions): List<String> {

        return listOfNotNull(
            listOfNotNull(
                TypescriptCommentRenderer.renderComment(
                    listOfNotNull(
                        documentation?.text,
                        documentation?.url?.let { "@url $it" },
                        documentation?.since?.let { "@since ${it.full()}" }
                    ),
                    options
                )
            ).joinToString(""),
            "$name: ${type.render(options)};"
        )
    }
}
