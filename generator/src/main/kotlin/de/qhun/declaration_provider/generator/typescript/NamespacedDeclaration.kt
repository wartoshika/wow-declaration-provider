package de.qhun.declaration_provider.generator.typescript

import de.qhun.declaration_provider.domain.Declaration
import de.qhun.declaration_provider.domain.Documentation

data class NamespacedDeclaration(
    override val name: String,
    override val score: Int,
    override val documentation: Documentation?,
    override val deprecated: Boolean?,
    override val namespace: String?,
    val declarations: List<Declaration>
) : Declaration
