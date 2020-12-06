package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.ConstantDeclaration
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions

object TypescriptConstantRenderer {

    fun ConstantDeclaration.render(options: DeclarationGeneratorOptions, enumContext: Boolean = false, namespaceContext: Boolean = false): List<String> {

        val data = mutableListOf(
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
        ).filter { it.isNotBlank() }.toMutableList()

        if (enumContext) {
            data.add("$name = ${numberOrString(value)}")
        } else {
            data.add("${if (namespaceContext) "" else "declare "}const $name = ${numberOrString(value)}")
        }

        return data
    }

    private fun numberOrString(value: Any): Any {
        return try {
            ("" + value).toDouble()
            value
        } catch (e: Throwable) {
            "\"$value\""
        }
    }
}
