package de.qhun.declaration_provider.domain

data class ParameterFragment(
    override val name: String? = null,
    override val score: Int = 0,
    override val documentation: Documentation? = null,
    val type: DataType,
    val canBeNull: Boolean? = null
) : Fragment
