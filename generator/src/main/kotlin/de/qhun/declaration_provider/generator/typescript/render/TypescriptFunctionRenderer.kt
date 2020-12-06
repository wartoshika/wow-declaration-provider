package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.FunctionDeclaration
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.render.TypescriptParameterRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptParameterRenderer.renderComment
import de.qhun.declaration_provider.generator.typescript.render.TypescriptReturnRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptReturnRenderer.renderComment

internal object TypescriptFunctionRenderer {

    fun FunctionDeclaration.render(
        options: DeclarationGeneratorOptions,
        namespaceContext: Boolean = false
    ): List<String> {

        return listOfNotNull(
            listOfNotNull(
                TypescriptCommentRenderer.renderComment(
                    listOfNotNull(
                        documentation?.text,
                        documentation?.url?.let { "@url $it" },
                        documentation?.since?.let { "@since ${it.full()}" },
                        *parameter.renderComment(options).toTypedArray(),
                        *returns.renderComment(options).toTypedArray()
                    ),
                    options
                )
            ).joinToString(""),
            listOfNotNull(
                if (namespaceContext) null else "declare ",
                "function ",
                name,
                "(",
                parameter.joinToString(", ") { it.render(options) },
                "): ",
                returns.render(options),
                ";"
            ).joinToString("")
        )
    }

}
