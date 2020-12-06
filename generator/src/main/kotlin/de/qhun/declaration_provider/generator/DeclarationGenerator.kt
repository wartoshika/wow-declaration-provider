package de.qhun.declaration_provider.generator

import de.qhun.declaration_provider.domain.Declaration

interface DeclarationGenerator {

    suspend fun generate(declarations: List<Declaration>, options: DeclarationGeneratorOptions): Map<String, String>
}
