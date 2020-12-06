package de.qhun.declaration_provider.generator

data class DeclarationGeneratorOptions(
    val lineBreakChar: String = "\n",
    val lineIntend: Int = 4,
    val declarationPadding: Int = 1
)
