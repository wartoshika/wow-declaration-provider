package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.DataType
import de.qhun.declaration_provider.domain.ReturnFragment
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.render.TypescriptDataTypeRenderer.render

object TypescriptReturnRenderer {

    fun ReturnFragment.render(options: DeclarationGeneratorOptions): String {
        return type.render(options)
    }

    fun List<ReturnFragment>.render(options: DeclarationGeneratorOptions): String {
        return when (size) {
            0 -> DataType.TypeNull.render(options)

            1 -> first().render(options)
            else -> listOf(
                "[",
                joinToString(", ") {
                    it.render(options)
                },
                "]"
            ).joinToString("")
        }
    }

    fun List<ReturnFragment>.renderComment(options: DeclarationGeneratorOptions): List<String> {
        return when (size) {
            0 -> listOf("@returns " + DataType.TypeNull.render(options))
            1 -> listOf("@returns " + first().render(options))
            else -> listOf(
                listOf("@returns Up to $size values"),
                mapIndexed { idx, it ->
                    " ${idx + 1}. ${it.name ?: ""} ${it.render(options)} ${it.documentation?.text?.let { txt -> "($txt)" } ?: ""}"
                }
            ).flatten()
        }
    }

}
