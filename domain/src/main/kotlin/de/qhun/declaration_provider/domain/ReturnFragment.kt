package de.qhun.declaration_provider.domain

data class ReturnFragment(
    override val name: String? = null,
    override val score: Int = 0,
    override val documentation: Documentation? = null,
    val type: DataType,
) : Fragment
