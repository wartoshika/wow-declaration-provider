package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.ParameterFragment
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.render.TypescriptDataTypeRenderer.render

internal object TypescriptParameterRenderer {

    fun ParameterFragment.render(options: DeclarationGeneratorOptions): String {
        return "$name: ${type.render(options)}"
    }

    fun ParameterFragment.renderComment(options: DeclarationGeneratorOptions): String {
        return "@param $name ${type.render(options)} ${documentation?.text ?: ""}"
    }

    fun List<ParameterFragment>.renderComment(options: DeclarationGeneratorOptions): List<String> {

        return map {
            it.renderComment(options)
        }
    }
}
