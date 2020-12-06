package de.qhun.declaration_provider.domain

data class EnumDeclaration(
    override val name: String,
    override val score: Int,
    override val documentation: Documentation?,
    override val deprecated: Boolean?,
    override val namespace: String?,
    val entries: List<ConstantDeclaration>
) : Declaration
