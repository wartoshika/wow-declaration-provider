package de.qhun.declaration_provider.generator.typescript.render

data class RenderResult(
    val fileName: String,
    val declarationSortableName: String,
    val code: String
)
