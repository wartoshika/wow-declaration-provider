package de.qhun.declaration_provider.domain

data class ConstantDeclaration(
    override val name: String,
    override val score: Int = 0,
    override val documentation: Documentation? = null,
    override val deprecated: Boolean? = null,
    override val namespace: String? = null,
    val value: String,
    val type: DataType,
) : Declaration
