package de.qhun.declaration_provider.generator.typescript.render

import de.qhun.declaration_provider.domain.*
import de.qhun.declaration_provider.generator.DeclarationGeneratorOptions
import de.qhun.declaration_provider.generator.typescript.NamespacedDeclaration
import de.qhun.declaration_provider.generator.typescript.render.TypescriptConstantRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptEnumRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptFunctionRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptNamespaceRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptInterfaceRenderer.render
import de.qhun.declaration_provider.generator.typescript.render.TypescriptPropertyRenderer.render

internal object TypescriptRenderer {

    fun render(
        declaration: Declaration,
        options: DeclarationGeneratorOptions,
        namespaceContext: Boolean
    ): List<String> = when (declaration) {
        is FunctionDeclaration -> declaration.render(options, namespaceContext)
        is NamespacedDeclaration -> declaration.render(options)
        is InterfaceDeclaration -> declaration.render(options, namespaceContext)
        is EnumDeclaration -> declaration.render(options, namespaceContext)
        is ConstantDeclaration -> declaration.render(options, namespaceContext)
        is PropertyDeclaration -> declaration.render(options)
        else -> {
            println("o Unable to render declaration because class ${declaration::class.simpleName} is unsupported")
            listOf()
        }
    }

}
